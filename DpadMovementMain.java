package teleop;
//package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp

public class Meca extends LinearOpMode {
    private DcMotor leftFront = null;
    private DcMotor rightFront = null;
    private DcMotor leftRear = null;
    private DcMotor rightRear = null;
    private DcMotor intakeMotor  = null;
    //private DcMotor intakeMotor2  = null;
    private DcMotor lanucherMotor  = null;

    //private DcMotor inTake = null;
    //private DcMotor sweeperMotor = null;
    //private DcMotor extendMotor = null;

    @Override
    public void runOpMode() {

        //declare variables for drive train motors
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        leftRear = hardwareMap.get(DcMotor.class,  "leftRear");
        rightRear = hardwareMap.get(DcMotor.class, "rightRear");
        
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        //intakeMotor2 = hardwareMap.get(DcMotor.class, "intakeMotor2");
        lanucherMotor = hardwareMap.get(DcMotor.class, "lanucherMotor");
        //inTake = hardwareMap.get(DcMotor.class, "inTake");

        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightRear.setDirection(DcMotor.Direction.REVERSE);

        //WHEELS BRAKE
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        telemetry.addData("Status", "Initialized, hi driver");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            //When code is active on the phone, telemetry will print
            telemetry.addData("Status", "Running");
            telemetry.update();

            // Set controller input
            double y = gamepad1.left_stick_y; // Remember, this is reversed!
            double x = gamepad1.left_stick_x; // this is strafing
            double rx = gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double FL_power = (y + x + rx) / denominator;
            double BL_power = (y - x + rx) / denominator;
            double FR_power = (y - x - rx) / denominator;
            double BR_power = (y + x - rx) / denominator;

            // Throttle speed
            if(gamepad1.right_trigger > 0) {
                FR_power *= Math.min(1, 1.5 - gamepad1.right_trigger);
                BR_power *= Math.min(1, 1.5 - gamepad1.right_trigger);
                FL_power *= Math.min(1, 1.5 - gamepad1.right_trigger);
                BL_power *= Math.min(1, 1.5 - gamepad1.right_trigger);
            }

            // Set motor powers
            // changeing the percenatge of how fast we go
            rightFront.setPower(FR_power*1);
            rightRear.setPower(BR_power*1);
            leftFront.setPower(FL_power*1);
            leftRear.setPower(BL_power*1);

           // sliders go up and down with limits of -6000 and 0 (SLIDER THAT GO UP AND DOWN)
                         // --- Intake control (A/B) ---
            if (gamepad1.a) {
                intakeMotor.setPower(1.0);
                //intakeMotor2.setPower(0.6);
            } else if (gamepad1.b) {
                intakeMotor.setPower(-1.0);
                //intakeMotor2.setPower(-0.6);
            } else {
                intakeMotor.setPower(0.0);
                //intakeMotor2.setPower(0.0);
            }

            // --- Launcher motor (RB/LB) ---
            //if (gamepad1.right_bumper) {
            //    lanucherMotor.setPower(1);  // Forward spin
            //} else if (gamepad1.left_bumper) {
            //    lanucherMotor.setPower(-1); // Reverse spin
            //} else {
             //   lanucherMotor.setPower(0.0);  // Stop
            //}
             // --- Launcher control (Trigger/Bumper) ---
            if (gamepad1.right_trigger > 0.1) {
                lanucherMotor.setPower(1);   // Forward spin
            } else if (gamepad1.right_bumper) {
                lanucherMotor.setPower(0.8);  // Reverse spin
            } else if (gamepad1.left_trigger > 0.1){
                lanucherMotor.setPower(-1);
            } else if (gamepad1.left_bumper) {
                lanucherMotor.setPower(-0.8); 
            } else {
                lanucherMotor.setPower(0.0);   // Stop
            }
    
