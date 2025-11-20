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
    double posx, posy;

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

        Pose2d pos1 = new Pose2d(posx, posy, 0);

        double twenty = pos1.position.x;

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
        }

        double fRightPower = driver + turn + strafe;
        double fLeftPower = (driver - turn - strafe);
        double bRightPower = driver + turn - strafe;
        double bLeftPower = (driver - turn + strafe);

        double oppositeSide = 5.0; // Example value
        double adjacentSide = 12.0; // Example value

        // Calculate the tangent ratio
        double tangentRatio = oppositeSide / adjacentSide;
        double angleInRadians = Math.atan(tangentRatio);
        double angleInDegrees = Math.toDegrees(angleInRadians);

        // apply power
        frontRightMotor.setPower(fRightPower);
        frontLeftMotor.setPower(fLeftPower);
        backRightMotor.setPower(bRightPower);
        backLeftMotor.setPower(bLeftPower);

        // quick debug telemetry
        telemetry.update();
        telemetry.addData("XCord", twenty);
        telemetry.addData("YCord", yCord);
        telemetry.addData("drive", driver);
        telemetry.addData("strafe", strafe);
        telemetry.addData("turn", turn);
        telemetry.addData("drive angle", drive_angle);

    }
}