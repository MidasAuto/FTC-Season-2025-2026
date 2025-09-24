package org.firstinspires.ftc.teamcode.roadrunner.messages;

import com.acmerobotics.roadrunner.ftc.PositionVelocityPair;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public final class IDriveLocalizerInputsMessage {
    public long timestamp;
    public PositionVelocityPair left;
    public PositionVelocityPair middle;
    public PositionVelocityPair right;
    public double yaw;
    public double pitch;
    public double roll;

    public IDriveLocalizerInputsMessage(PositionVelocityPair left, PositionVelocityPair middle, PositionVelocityPair right,  YawPitchRollAngles angles) {
        this.timestamp = System.nanoTime();
        this.left = left;
        this.middle = middle;
        this.right = right;
        {
            this.yaw = angles.getYaw(AngleUnit.RADIANS);
            this.pitch = angles.getPitch(AngleUnit.RADIANS);
            this.roll = angles.getRoll(AngleUnit.RADIANS);
        }
    }
}
