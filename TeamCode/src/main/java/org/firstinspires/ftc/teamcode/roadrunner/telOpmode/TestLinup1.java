package org.firstinspires.ftc.teamcode.roadrunner.telOpmode;

import static org.firstinspires.ftc.teamcode.roadrunner.autoOpmode.Auto1.PoseStorage.currentPose;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.acmerobotics.roadrunner.Vector2d;
import org.firstinspires.ftc.teamcode.roadrunner.PinpointDrive;
import org.firstinspires.ftc.teamcode.roadrunner.Localizer;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.TwoDeadWheelLocalizer;
import org.firstinspires.ftc.teamcode.roadrunner.autoOpmode.Auto1;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive.DriveLocalizer;

@TeleOp(name = "LinupTest")
public class TestLinup1 extends OpMode {

    DcMotor frontRightMotor, backLeftMotor, frontLeftMotor, backRightMotor;
    double posx, posy, heading;
    double targetDistance, targetX, targetY;
    Pose2d startPose = currentPose;
    PinpointDrive drive = new PinpointDrive(hardwareMap, startPose);

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

        drive.pinpoint.update();

        posx = drive.pinpoint.getPosX();
        posy = drive.pinpoint.getPosY();
        heading = drive.pinpoint.getHeading();

        targetX = (100 - posx)*(100-posx);
        targetY = (100 - posy)*(100-posy);

        targetDistance = (Math.sqrt(targetX+targetY));




        drive.pinpoint.update();

        double driver = gamepad1.left_stick_y; // forward/back
        double strafe = gamepad1.left_stick_x;  // left/right
        double turn = -gamepad1.right_stick_x;   // rotation

        boolean shotLinup = gamepad1.dpad_up;

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
        telemetry.addData("X", posx);
        telemetry.addData("Y", posy);
        telemetry.addData("drive", driver);
        telemetry.addData("strafe", strafe);
        telemetry.addData("turn", turn);
        telemetry.addData("drive angle", drive_angle);

    }
}