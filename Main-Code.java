package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Basic: Omni Linear OpMode with Dual Intakes + Launcher (Bumpers) + Servo", group="Linear OpMode")
public class BasicOmniOpMode_Linear extends LinearOpMode {

    // Declare OpMode members
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;
    private DcMotor intakeMotor = null;
    private DcMotor intakeMotor2 = null; // 🔹 Second intake motor
    private DcMotor launcherMotor = null;
    private Servo launcherServo = null;

    // Servo positions
    private final double SERVO_REST = 0.0;
    private final double SERVO_FIRE = 0.5;

    // Deadzone for joystick drift and motor threshold
    private final double DEADZONE = 0.05;
    private final double MOTOR_THRESHOLD = 0.05; // 🔹 Below this, motor power = 0

    @Override
    public void runOpMode() {

        // Initialize hardware
        frontLeftDrive  = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        backLeftDrive   = hardwareMap.get(DcMotor.class, "backLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backRightDrive  = hardwareMap.get(DcMotor.class, "backRightDrive");
        intakeMotor     = hardwareMap.get(DcMotor.class, "intakeMotor");
        intakeMotor2    = hardwareMap.get(DcMotor.class, "intakeMotor2");
        launcherMotor   = hardwareMap.get(DcMotor.class, "launcherMotor");
        launcherServo   = hardwareMap.get(Servo.class, "launcherServo");

        // --- Motor Directions ---
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);
        intakeMotor.setDirection(DcMotor.Direction.FORWARD);
        intakeMotor2.setDirection(DcMotor.Direction.FORWARD);
        launcherMotor.setDirection(DcMotor.Direction.FORWARD);

        // --- Stop all motors and reset servo before start ---
        frontLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backLeftDrive.setPower(0);
        backRightDrive.setPower(0);
        intakeMotor.setPower(0);
        intakeMotor2.setPower(0);
        launcherMotor.setPower(0);
        launcherServo.setPosition(SERVO_REST);

        telemetry.addData("Status", "Initialized - Motors stopped");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            // --- Read gamepad sticks ---
            double axial   = -gamepad1.left_stick_y;  // Forward/backward
            double lateral =  gamepad1.left_stick_x;  // Left/right
            double yaw     =  gamepad1.right_stick_x; // Rotation

            // --- Apply deadzone to prevent drift ---
            if (Math.abs(axial) < DEADZONE) axial = 0;
            if (Math.abs(lateral) < DEADZONE) lateral = 0;
            if (Math.abs(yaw) < DEADZONE) yaw = 0;

            // --- Calculate wheel powers ---
            double frontLeftPower  = axial + lateral + yaw;
            double frontRightPower = axial + lateral - yaw;
            double backLeftPower   = axial + lateral + yaw;
            double backRightPower  = axial - lateral - yaw;

            // --- Normalize powers ---
            double max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
            max = Math.max(max, Math.abs(backLeftPower));
            max = Math.max(max, Math.abs(backRightPower));
            if (max > 1.0) {
                frontLeftPower  /= max;
                frontRightPower /= max;
                backLeftPower   /= max;
                backRightPower  /= max;
            }

            // --- Apply motor threshold to ensure no movement on tiny inputs ---
            if (Math.abs(frontLeftPower) < MOTOR_THRESHOLD) frontLeftPower = 0;
            if (Math.abs(frontRightPower) < MOTOR_THRESHOLD) frontRightPower = 0;
            if (Math.abs(backLeftPower) < MOTOR_THRESHOLD) backLeftPower = 0;
            if (Math.abs(backRightPower) < MOTOR_THRESHOLD) backRightPower = 0;

            // --- Drive motors ---
            frontLeftDrive.setPower(frontLeftPower);
            frontRightDrive.setPower(frontRightPower);
            backLeftDrive.setPower(backLeftPower);
            backRightDrive.setPower(backRightPower);

            // --- Intake control (A/B) ---
            if (gamepad1.a) {
                intakeMotor.setPower(1.0);
                intakeMotor2.setPower(0.6);
            } else if (gamepad1.b) {
                intakeMotor.setPower(-1.0);
                intakeMotor2.setPower(-0.6);
            } else {
                intakeMotor.setPower(0.0);
                intakeMotor2.setPower(0.0);
            }

            // --- Launcher motor (RB/LB) ---
            if (gamepad1.right_bumper) {
                launcherMotor.setPower(0.8);  // Forward spin
            } else if (gamepad1.left_bumper) {
                launcherMotor.setPower(-0.8); // Reverse spin
            } else {
                launcherMotor.setPower(0.0);  // Stop
            }

            // --- Launcher Servo (X/Y) ---
            if (gamepad1.x) {
                launcherServo.setPosition(SERVO_FIRE);
            } else if (gamepad1.y) {
                launcherServo.setPosition(SERVO_REST);
            }

            // --- Telemetry ---
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Drive Power", "FL(%.2f) FR(%.2f) BL(%.2f) BR(%.2f)",
                    frontLeftPower, frontRightPower, backLeftPower, backRightPower);
            telemetry.addData("Axial", "%.2f", axial);
            telemetry.addData("Lateral", "%.2f", lateral);
            telemetry.addData("Yaw", "%.2f", yaw);
            telemetry.addData("Intake1", "Power: %.2f", intakeMotor.getPower());
            telemetry.addData("Intake2", "Power: %.2f", intakeMotor2.getPower());
            telemetry.addData("Launcher", "Power: %.2f", launcherMotor.getPower());
            telemetry.addData("Servo", "Position: %.2f", launcherServo.getPosition());
            telemetry.update();
        }
    }
}
