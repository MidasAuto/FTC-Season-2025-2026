package org.firstinspires.ftc.teamcode.roadrunner.telOpmode;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.acmerobotics.roadrunner.Vector2d;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
@TeleOp(name = "LinupTest")
public class TestLinup1 extends OpMode {

    DcMotor frontRightMotor;
    DcMotor frontLeftMotor;
    DcMotor backRightMotor;
    DcMotor backLeftMotor;

    // Tweak this slightly if the left side still feels faster (ex: 0.95 -> 0.92)
    @Override
    public void init() {
        frontRightMotor = hardwareMap.get(DcMotor.class, "rightFront");
        frontLeftMotor  = hardwareMap.get(DcMotor.class, "leftFront");
        backLeftMotor   = hardwareMap.get(DcMotor.class, "leftBack");
        backRightMotor  = hardwareMap.get(DcMotor.class, "rightBack");

        // ORIGINAL: only frontLeft reversed
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        // backLeftMotor left as default (no reverse)

        // Brake so robot stops instead of coasting
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Use encoders like your original code
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {
        // Example: Getting the current estimated pose
        double xCord = 0;
        double yCord = 0;
        Vector2d vector2d = new Vector2d(xCord, yCord);

        double driver = gamepad1.left_stick_y; // forward/back
        double strafe = gamepad1.left_stick_x;  // left/right
        double turn = -gamepad1.right_stick_x;   // rotation

        boolean shotLinup = gamepad1.dpad_up;

        double targetCordx = 100;
        double targetCordy = 100;


        double adj = targetCordx - xCord;
        double opp = targetCordy - yCord;

        double actualAngle = 180;
        double targetAngle = Math.atan2(opp, adj);
        double andgleDiff = targetAngle - actualAngle;

        double drive_angle = 0;
        if ((shotLinup) && (gamepad1.left_stick_y != 0 || gamepad1.left_stick_x != 0)) {
            drive_angle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x);

            double r = Math.sqrt(gamepad1.left_stick_x * gamepad1.left_stick_x + gamepad1.left_stick_x * gamepad1.left_stick_x); // sqrt(2) ≈ 1.414

            driver = r * Math.cos(drive_angle);
            strafe = r * Math.sin(drive_angle);
        }

        if (targetAngle - actualAngle > 5) {
            turn = (andgleDiff / 10) * (andgleDiff / 10) / (andgleDiff / 10) * (andgleDiff / 10);
        } else if (targetAngle - actualAngle < -5) {
            turn = -(andgleDiff / 10) * (andgleDiff / 10) * (andgleDiff / 10) * (andgleDiff / 10);
        }
        // keep your original stick mapping

        // motor power calc with left-side compensation
        double fRightPower = driver + turn + strafe;
        double fLeftPower = (driver - turn - strafe);
        double bRightPower = driver + turn - strafe;
        double bLeftPower = (driver - turn + strafe);

        // normalize so no value exceeds ±1
        double max = Math.max(1.0,
                Math.max(Math.abs(fRightPower),
                        Math.max(Math.abs(fLeftPower),
                                Math.max(Math.abs(bRightPower), Math.abs(bLeftPower)))));

        fRightPower /= max;
        fLeftPower /= max;
        bRightPower /= max;
        bLeftPower /= max;

        double oppositeSide = 5.0; // Example value
        double adjacentSide = 12.0; // Example value

        // Calculate the tangent ratio
        double tangentRatio = oppositeSide / adjacentSide;

        // Calculate the angle in radians using Math.atan()
        double angleInRadians = Math.atan(tangentRatio);

        // Convert the angle from radians to degrees (optional, for readability)
        double angleInDegrees = Math.toDegrees(angleInRadians);

        // apply power
        frontRightMotor.setPower(fRightPower);
        frontLeftMotor.setPower(fLeftPower);
        backRightMotor.setPower(bRightPower);
        backLeftMotor.setPower(bLeftPower);

        // quick debug telemetry
        telemetry.update();
        telemetry.addData("XCord", xCord);
        telemetry.addData("YCord", yCord);
        telemetry.addData("drive", driver);
        telemetry.addData("strafe", strafe);
        telemetry.addData("turn", turn);
        telemetry.addData("drive angle", drive_angle);

    }
}