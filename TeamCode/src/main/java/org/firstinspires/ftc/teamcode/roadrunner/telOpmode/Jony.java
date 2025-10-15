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
    ColorSensor checkColorSensor;
    ColorSensor initColorSensor;
    Servo servo;
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
    ElapsedTime timer = new ElapsedTime();
    boolean timer_running = false;
    double targetTime = 0.0;

    // Tweak this slightly if the left side still feels faster (ex: 0.95 -> 0.92)
    double leftMotorSpeedFactor = 0.95;

    @Override
    public void init() {
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRightMotor");
        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeftMotor");
        backRightMotor = hardwareMap.get(DcMotor.class, "backRightMotor");
        sorterMotor = hardwareMap.get(DcMotor.class, "sorterMotor");
        //Color sensor
        checkColorSensor = hardwareMap.get(ColorSensor.class, "checkColorSensor");
        initColorSensor = hardwareMap.get(ColorSensor.class, "initColorSensor");
        //Servos
        servo = hardwareMap.get(Servo.class, "leverServo");

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
        if (checkForBall()); {
            checkColor();
        }

        // Use servo
        if (gamepad1.dpad_up) {
            servo.setPosition(0.0);  // move to one end
        } else if (gamepad1.dpad_down) {
            servo.setPosition(1.0);  // move to the other end
        }
        telemetry.addData("Servo Position", servo.getPosition());
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
        telemetry.update();
    }
    public void checkColor() {
        getColor();// Get values

        if (blueValue < greenValue) {
            greenTrue = true;
        }
        if (greenValue < blueValue) {
            purpleTrue = true;
        }
        if (holderOne.get(0) == 0) {
            if (greenTrue) {
                holderOne.set(1, 1);
            } else {
                holderOne.set(1, 2);
            }
        } else if (holderTwo.get(0) == 0) {
            if (greenTrue) {
                holderTwo.set(1, 1);
            } else {
                holderTwo.set(1, 2);
            }
        } else if (holderThree.get(0) == 0) {
            if (greenTrue) {
                holderThree.set(1, 1);
            } else {
                holderThree.set(1, 2);
            }
            greenTrue = false;
            purpleTrue = false;
        }
    }
    public boolean checkForBall() {
        int redV = checkColorSensor.red();
        int blueV = checkColorSensor.blue();
        int greenV = checkColorSensor.green();
        int alphaV = checkColorSensor.alpha();

        return redV > 1000 && blueV > 1000 && greenV > 1000;
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