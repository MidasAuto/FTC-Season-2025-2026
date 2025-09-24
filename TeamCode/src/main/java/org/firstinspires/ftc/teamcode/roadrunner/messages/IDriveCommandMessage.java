package org.firstinspires.ftc.teamcode.roadrunner.messages;

public final class IDriveCommandMessage {
    public long timestamp;
    public double voltage;
    public double rightPower;
    public double middlePower;
    public double leftPower;

    public IDriveCommandMessage(double voltage, double leftPower, double middlePower, double rightPower) {
        this.timestamp = System.nanoTime();
        this.voltage = voltage;
        this.rightPower = rightPower;
        this.leftPower = leftPower;
        this.middlePower = middlePower;

    }
}

