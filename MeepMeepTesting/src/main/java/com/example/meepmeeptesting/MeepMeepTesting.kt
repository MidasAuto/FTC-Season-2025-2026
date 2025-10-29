package com.example.meepmeeptesting

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.Vector2d
import com.noahbres.meepmeep.MeepMeep
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity

object MeepMeepTesting {
    @JvmStatic
    fun main(args: Array<String>) {
        val meepMeep: MeepMeep = MeepMeep(800)
        val beginPose = Pose2d(30.5, 66.0, -Math.PI / 2)

        val myBot: RoadRunnerBotEntity =
            DefaultBotBuilder(meepMeep) // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60.0, 60.0, Math.toRadians(180.0), Math.toRadians(180.0), 15.0)
                .build()

        val drive = myBot.drive;

        myBot.runAction(
            SequentialAction(
                drive.actionBuilder(beginPose)
                    .setTangent(Math.PI)
                    .splineToConstantHeading(Vector2d(2.0, 40.0), -Math.PI / 2)
                    .strafeTo(Vector2d(2.0, 30.0))
                    .build(),
                drive.actionBuilder(Pose2d(2.0, 30.0, -Math.PI/2))
                    .setTangent(Math.PI / 2)
                    .splineToConstantHeading(Vector2d(-24.0, 54.0), Math.PI)
                    .splineToConstantHeading(Vector2d(-24.0, 48.0), Math.PI)
                    .splineToConstantHeading(Vector2d(-41.5, 64.0), Math.PI)
                    .build()
            )
        )

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_LIGHT)
            .setDarkMode(true)
            .setBackgroundAlpha(0.95f)
            .addEntity(myBot)
            .start()
    }
}