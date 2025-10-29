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
    ColorSensor checkColorSensor;


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
    boolean shootMode = false;

    ElapsedTime timer = new ElapsedTime();
    boolean timer_running = false;
    double targetTime = 0.0;

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

        // Use encoders like your original code
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    @Override
    public void loop() {
        // keep your original stick mapping
        double drive = gamepad1.left_stick_y; // forward/back
        double strafe = gamepad1.left_stick_x;  // left/right
        double turn = -gamepad1.right_stick_x;   // rotation

        // motor power calc with left-side compensation
        double fRightPower = drive + turn + strafe;
        double fLeftPower = (drive - turn - strafe) * leftMotorSpeedFactor;
        double bRightPower = drive + turn - strafe;
        double bLeftPower = (drive - turn + strafe) * leftMotorSpeedFactor;

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

        //Read color sensor value
        if (checkForBall() && !sorterMoving); {
            checkColor();
        }
        if (!shootMode && gamepad1.dpad_up) {
            intakeMoter.setPower(1);
        } else {
            intakeMoter.setPower(0);
        }

        telemetry();
        telemetry.update();


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
        telemetry.addData("CheckForBall", checkForBall());
        telemetry.addData("holder value: ", holderOne.get(1));
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
    public void move_sorter(int position, int target) {
        if (!timer_running) {
            if (position == target) {
                return;
            } else if (position < target) {
                sorterMotor.setPower(1);
                timer.reset();
                targetTime = 1.0;
                timer_running = true;
            } else if (position > target) {
                if (target == 0) {
                    sorterMotor.setPower(1);
                    timer.reset();
                    targetTime = 1.0;
                    timer_running = true;
                } else if (target == 1) {
                    sorterMotor.setPower(1);
                    timer.reset();
                    targetTime = 2.0;
                    timer_running = true;
                }
            }
        }
        else if (timer.seconds() >= targetTime) {
            timer_running = false;
            sorterMotor.setPower(0);
        }
    }
}