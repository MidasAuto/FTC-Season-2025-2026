package org.firstinspires.ftc.teamcode.roadrunner.telOpmode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

@TeleOp(name = "colorSensorTest")
public class ColorSensorTest extends OpMode {
    private ColorSensor colorSensor;
    private double redValue = 1;
    private double blueValue = 1;
    private double greenValue = 1;
    private double alphaValue = 1;
    private double targetValue = 1000;

    public void getColor() {
        redValue = colorSensor.red();
        blueValue = colorSensor.blue();
        greenValue = colorSensor.green();
        alphaValue = colorSensor.alpha();
    }


    public void initColorSensor() {
        colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");

    }

    @Override
    public void init() {
        initColorSensor();
    }
    @Override
    public void loop() {
        boolean T = true;
        while(T) {
            telemetry();
            getColor();
        }


    }

    public void telemetry() {
        telemetry.addData("redValue", redValue);
        telemetry.addData("blueValue", blueValue);
        telemetry.addData("greenValue", greenValue);
        telemetry.addData("alphaValue", alphaValue);
        telemetry.update();
    }
}
