package org.firstinspires.ftc.teamcode.roadrunner.telOpmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;


@TeleOp(name = "Jony")
public class Jony extends OpMode {

    DcMotor frontRightMotor;
    DcMotor frontLeftMotor;
    DcMotor backRightMotor;
    DcMotor backLeftMotor;
    DcMotor sorterMotor;
    DcMotor intakeMoter;
    DcMotor launch1;
    DcMotor launch2;
    ColorSensor checkColorSensor;
    Servo launchServo;
    Servo rampAngle;


    double redValue = 1;
    double blueValue = 1;
    double greenValue = 1;
    double alphaValue = 1;
    boolean greenTrue = false;
    boolean purpleTrue = false;
    //Ball Positions
    List<Integer> holderOne = new ArrayList<>(Arrays.asList(0,0));
    List<Integer> holderTwo = new ArrayList<>(Arrays.asList(1,0));
    List<Integer> holderThree = new ArrayList<>(Arrays.asList(2,0));
    //timer
    boolean sorterMoving = false;
    int target_value = 0;
    boolean shootMode = false;
    boolean xpreveous = false;
    boolean leftBumperPreveous = false;
    boolean rightBumberPreveous = false;

    // Tweak this slightly if the left side still feels faster (ex: 0.95 -> 0.92)
    double leftMotorSpeedFactor = 0.95;

    @Override
    public void init() {
        frontRightMotor = hardwareMap.get(DcMotor.class, "rightFront");
        frontLeftMotor = hardwareMap.get(DcMotor.class, "leftFront");
        backLeftMotor = hardwareMap.get(DcMotor.class, "leftBack");
        backRightMotor = hardwareMap.get(DcMotor.class, "rightBack");
        sorterMotor = hardwareMap.get(DcMotor.class, "spedMotor");
        intakeMoter = hardwareMap.get(DcMotor.class, "intakeMotor");
        launch1 = hardwareMap.get(DcMotor.class, "launch1");
        launch2 = hardwareMap.get(DcMotor.class, "launch2");
        //servos
        launchServo = hardwareMap.get(Servo.class, "launchServo");
        rampAngle = hardwareMap.get(Servo.class, "rampAngle");
        //Color sensor
        checkColorSensor = hardwareMap.get(ColorSensor.class, "checkColorSensor");

        // ORIGINAL: only frontLeft reversed
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        // backLeftMotor left as default (no reverse)

        // Brake so robot stops instead of coasting
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        sorterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Use encoders like your original code
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ///sorterMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        target_value = sorterMotor.getCurrentPosition();

    }

    @Override
    public void loop() {
        boolean xcurrent = gamepad2.x;
        boolean leftBumperCurrent = gamepad2.left_bumper;
        boolean rightBumperCurrent = gamepad2.right_bumper;
        // keep your original stick mapping
        double drive = gamepad1.left_stick_y; // forward/back
        double strafe = gamepad1.left_stick_x;  // left/right
        double turn = gamepad1.right_stick_x;   // rotation

        // motor power calc with left-side compensation
        double fRightPower = drive + turn + strafe;
        double fLeftPower = (drive - turn - strafe);
        double bRightPower = drive + turn - strafe;
        double bLeftPower = (drive - turn + strafe);

        // normalize so no value exceeds ±1
        double max = Math.max(1.0,
                Math.max(Math.abs(fRightPower),
                        Math.max(Math.abs(fLeftPower),
                                Math.max(Math.abs(bRightPower), Math.abs(bLeftPower)))));

        fRightPower /= max;
        fLeftPower /= max;
        bRightPower /= max;
        bLeftPower /= max;

        // apply power
        frontRightMotor.setPower(fRightPower);
        frontLeftMotor.setPower(fLeftPower);
        backRightMotor.setPower(bRightPower);
        backLeftMotor.setPower(bLeftPower);
        rampAngle.setPosition(0.5);

        //Read color sensor value
        if (checkForBall() && !sorterMoving && !shootMode) {
            checkColor();
        }


        if (xcurrent && !xpreveous) {
            if (shootMode) {
                shootMode = false;
                target_value += 90;
                sorterMoving = true;
            }
            else {
                shootMode = true;
                target_value -= 90;
                sorterMoving = true;
            }
        }
        if (leftBumperCurrent && !leftBumperPreveous) {
            target_value -= 180;
            sorterMoving = true;
        }
        if (rightBumperCurrent && !rightBumberPreveous) {
            target_value += 180;
            sorterMoving = true;
        }
        if (gamepad1.left_trigger > 0) {
            intakeMoter.setPower(1);
        } else if (gamepad1.right_trigger > 0) {
            intakeMoter.setPower(-1);
        } else {
            intakeMoter.setPower(0);
        }
        if (gamepad2.right_trigger > 0 && shootMode) {
            launch1.setPower(1);
            launch2.setPower(1);
        } else if (gamepad2.left_trigger > 0 && shootMode){
            launch1.setPower(-1);
            launch2.setPower(-1);
        }
        else {
            launch1.setPower(0);
            launch2.setPower(0);
        }
        if (gamepad2.y && shootMode) {
            launchServo.setPosition(0);
        } else {
            launchServo.setPosition(1);
        }

        if (sorterMoving) {
            moveSorter();
        }

        telemetry();
        telemetry.update();
        xpreveous = xcurrent;
        leftBumperPreveous = leftBumperCurrent;
        rightBumberPreveous = rightBumperCurrent;
        checkPos();

    }
    public void getColor() {
        redValue = checkColorSensor.red();
        blueValue = checkColorSensor.blue();
        greenValue = checkColorSensor.green();
        alphaValue = checkColorSensor.alpha();
    }
    public void telemetry() {
        telemetry.addData("Is it Green?", greenTrue);
        telemetry.addData("Is it purple?", purpleTrue);
        telemetry.addData("Red: ", redValue);
        telemetry.addData("Green: ", greenValue);
        telemetry.addData("Blue: ", blueValue);
        telemetry.addData("SorterMoving", sorterMoving);
        telemetry.addData("Sorter Pos", sorterMotor.getCurrentPosition());
        telemetry.addData("target Pos", target_value);
        /*telemetry.addData("CheckForBall", checkForBall());
        telemetry.addData("holder value: ", holderOne.get(1))
         */
        telemetry.update();
    }
    public void checkColor() {
        getColor();// Get values

        if (blueValue < greenValue) {
            greenTrue = true;
            purpleTrue = false;
        }

        if (greenValue < blueValue) {
            purpleTrue = true;
            greenTrue = false;
        }
        if (holderOne.get(0) == 0) {
            if (greenTrue && checkForBall()) {
                holderOne.set(1, 1);
            } else if(purpleTrue && checkForBall()){
                holderOne.set(1, 2);
            }
        } else if (holderTwo.get(0) == 0) {
            if (greenTrue && checkForBall()) {
                holderTwo.set(1, 1);
            } else if (purpleTrue && checkForBall()){
                holderTwo.set(1, 2);
            }
        } else if (holderThree.get(0) == 0) {
            if (greenTrue && checkForBall()) {
                holderThree.set(1, 1);
            } else if(purpleTrue && checkForBall()){
                holderThree.set(1, 2);
            }
            greenTrue = false;
            purpleTrue = false;
        }
    }

    public boolean checkForBall() {;
        int blueV = checkColorSensor.blue();


        return blueV > 100 && !sorterMoving;
    }
    public void moveSorter() {
        double currpos = sorterMotor.getCurrentPosition();

        if (currpos <= target_value-7 || currpos >= target_value+7) {
            if (currpos < target_value) {
                sorterMotor.setPower(0.3);
            }
            else if (currpos > target_value) {
                sorterMotor.setPower(-0.3);
            }
        }
        else if (currpos >= target_value-7 && currpos <= target_value+7){
            sorterMotor.setPower(0);
            sorterMoving = false;
        }
    }
    public void checkPos() {
        int currpos = sorterMotor.getCurrentPosition();
        if (currpos <= target_value-7 || currpos >= target_value+7) {
            sorterMoving = true;
        }
    }
}