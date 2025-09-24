package org.firstinspires.ftc.teamcode.roadrunner
import com.acmerobotics.roadrunner.DualNum
import com.acmerobotics.roadrunner.Twist2dDual
import com.acmerobotics.roadrunner.Vector2dDual
import com.acmerobotics.roadrunner.PoseVelocity2dDual
import com.acmerobotics.roadrunner.VelConstraint
import com.acmerobotics.roadrunner.Pose2dDual
import com.acmerobotics.roadrunner.Arclength
import com.acmerobotics.roadrunner.PosePath




import kotlin.math.abs

/**
 * @param[trackWidth] distance between wheels on opposite sides; see the diagram below
 * ![Wheelbase and track width diagram](https://upload.wikimedia.org/wikipedia/commons/5/52/Wheelbase_and_Track.png)
 * @param[lateralMultiplier] factor that multiplies strafe velocity to compensate for slip; increase it to boost the
 * distance traveled in the strafe direction
 */
data class KinematicsIDrive @JvmOverloads constructor(
    @JvmField
    val trackWidth: Double,
    @JvmField
    val lateralMultiplier: Double = 1.0
) {
    /**
     * @param[wheelbase] distance between wheels on the same side; see the diagram in [KinematicsIDrive]
     */
    constructor(
        trackWidth: Double,
        wheelbase: Double,
        lateralMultiplier: Double = 1.0
    ) : this((trackWidth + wheelbase) / 2, lateralMultiplier)

    data class WheelIncrements<Param>(
        @JvmField
        val middle: DualNum<Param>,
        @JvmField
        val right: DualNum<Param>,
        @JvmField
        val left: DualNum<Param>,
    )

    fun <Param> forward(w: WheelIncrements<Param>) = Twist2dDual(
        Vector2dDual(
            (-w.left + w.right) * 0.5,
            (w.middle),
        ),
        (-w.left + w.right ) * (0.5 / trackWidth),
    )

    data class WheelVelocities<Param>(
        @JvmField
        val middle: DualNum<Param>,
        @JvmField
        val right: DualNum<Param>,
        @JvmField
        val left: DualNum<Param>,
    ) {
        fun all() = listOf(left, right, middle)
    }

    fun <Param> inverse(t: PoseVelocity2dDual<Param>) = WheelVelocities(
        t.linearVel.y,
        t.linearVel.x-t.angVel,
        t.linearVel.x+t.angVel
    )

    inner class WheelVelConstraint(@JvmField val maxWheelVel: Double) : VelConstraint {
        override fun maxRobotVel(robotPose: Pose2dDual<Arclength>, path: PosePath, s: Double): Double {
            val txRobotWorld = robotPose.value().inverse()
            val robotVelWorld = robotPose.velocity().value()
            val robotVelRobot = txRobotWorld * robotVelWorld
            return inverse(PoseVelocity2dDual.constant<Arclength>(robotVelRobot, 1))
                .all()
                .minOf { abs(maxWheelVel / it.value()) }
        }
    }
}

/**
 * @param[trackWidth] distance between wheels on opposite sides; see the diagram below
 * ![Wheelbase and track width diagram](https://upload.wikimedia.org/wikipedia/commons/5/52/Wheelbase_and_Track.png)
 */
data class TankKinematics(@JvmField val trackWidth: Double) {
    data class WheelIncrements<Param>(
        @JvmField
        val left: DualNum<Param>,
        @JvmField
        val right: DualNum<Param>,
    )

    fun <Param> forward(w: WheelIncrements<Param>) = Twist2dDual(
        Vector2dDual(
            (w.left + w.right) * 0.5,
            DualNum.constant(0.0, w.left.size()),
        ),
        (-w.left + w.right) / trackWidth,
    )

    data class WheelVelocities<Param>(
        @JvmField
        val left: DualNum<Param>,
        @JvmField
        val right: DualNum<Param>,
    ) {
        fun all() = listOf(left, right)
    }

    fun <Param> inverse(t: PoseVelocity2dDual<Param>): WheelVelocities<Param> {
        require(t.linearVel.y.values().all { abs(it) < 1e-6 }) {
            "Tank drive does not support lateral motion"
        }

        return WheelVelocities(
            t.linearVel.x - t.angVel * 0.5 * trackWidth,
            t.linearVel.x + t.angVel * 0.5 * trackWidth,
        )
    }

    // TODO: can probably be made generic, though lack of associated types may pose a difficulty
    inner class WheelVelConstraint(@JvmField val maxWheelVel: Double) : VelConstraint {
        override fun maxRobotVel(robotPose: Pose2dDual<Arclength>, path: PosePath, s: Double): Double {
            val txRobotWorld = robotPose.value().inverse()
            val robotVelWorld = robotPose.velocity().value()
            val robotVelRobot = txRobotWorld * robotVelWorld
            return inverse(PoseVelocity2dDual.constant<Arclength>(robotVelRobot, 1))
                .all()
                .minOf { abs(maxWheelVel / it.value()) }
        }
    }
}