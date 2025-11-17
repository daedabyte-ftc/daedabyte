package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

// Main class for TeleOp and Autonomous modes
@TeleOp
public class Auto extends LinearOpMode {
    // Drivetrain
    private DcMotor LeftDrive;
    private DcMotor RightDrive;
    private DcMotor fRightDrive;
    private DcMotor fLeftDrive;
    
    // Spool
    private DcMotor spool;
    
    // Bucket
    private Servo bucket;
    private double bucketPosition = 0;
    private long bucketPreviousUpdateTime = 0;
    private double bucketUpdateInterval = 200;
    
    // COREHEX
    private DcMotor Corehex1;
    private DcMotor Corehex2;
    double corehexDownPower = 0.3;
    double corehexUpPower = 0.3;
    
    // ElapsedTime for timing purposes
    private ElapsedTime runtime = new ElapsedTime();

    // Auto op mode (call to move and turn)
    public void moveAndTurn(double yPower, double xPower, int time) {
        // Start the timer
        runtime.reset();

        // Combine forward/backward and turn power
        double leftMotorPower = yPower + xPower;   // Left side motors
        double rightMotorPower = yPower - xPower;  // Right side motors
        
        // Apply combined power to motors
        fLeftDrive.setPower(leftMotorPower);
        LeftDrive.setPower(leftMotorPower);
        RightDrive.setPower(rightMotorPower);
        fRightDrive.setPower(rightMotorPower);

        // Debug print statement
        System.out.println("Move and Turn: ForwardPower: " + yPower + " TurnPower: " + xPower + " For: " + time);

        // Wait using ElapsedTime (non-blocking)
        while (runtime.milliseconds() < time * 1000) {
            // The loop will continue until the specified time has passed
        }

        // Stop motors after the specified time has passed
        LeftDrive.setPower(0);
        RightDrive.setPower(0);
        fLeftDrive.setPower(0);
        fRightDrive.setPower(0);
    }

    // Auto op mode (call to rotate)
    public void turnLeftRight(double xPower, int time) {
        // Start the timer
        runtime.reset();

        // Start motors
        fLeftDrive.setPower(xPower); 
        LeftDrive.setPower(xPower);
        RightDrive.setPower(-1 * xPower);
        fRightDrive.setPower(-1 * xPower);
        
        // Debug
        System.out.println("Set X: " + xPower + " For: " + time);
        
        // Wait using ElapsedTime (non-blocking)
        while (runtime.milliseconds() < time * 1000) {
            // The loop will continue until the specified time has passed
        }

        // Stop motors after the specified time has passed
        LeftDrive.setPower(0);
        RightDrive.setPower(0);
        fLeftDrive.setPower(0);
        fRightDrive.setPower(0);
    }

    // Auto op mode (call to move forward and backward)
    public void moveNorthSouth(double yPower, int time) {
        // Start the timer
        runtime.reset();

        // Start motors
        fLeftDrive.setPower(yPower); 
        LeftDrive.setPower(yPower);
        RightDrive.setPower(yPower);
        fRightDrive.setPower(yPower);
        
        // Debug
        System.out.println("Set Y: " + yPower + " For: " + time);
        
        // Wait using ElapsedTime (non-blocking)
        while (runtime.milliseconds() < time) {
            // The loop will continue until the specified time has passed
        }

        // Stop motors after the specified time has passed
        LeftDrive.setPower(0);
        RightDrive.setPower(0);
        fLeftDrive.setPower(0);
        fRightDrive.setPower(0);
    }
    
    // Spool
    public void spoolUpDown(double power, int time) {
        // Start the timer
        runtime.reset();

        // Start motors
        spool.setPower(power); 
        
        // Debug
        System.out.println("Spool power: " + power + " For: " + time);
        
        // Wait using ElapsedTime (non-blocking)
        while (runtime.milliseconds() < time) {
            // The loop will continue until the specified time has passed
        }
        // Stop motors after the specified time has passed
        // Start motors
        spool.setPower(0);
        telemetry.addData("Spool", power);
        telemetry.update();
        spool.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    
    // Bucket
    public void dumpBucket(int time) {
        // Bucket
        bucket.setPosition(0.6);
        telemetry.addData("Bucket Pos", bucketPosition);
        telemetry.update();
        // Wait using ElapsedTime (non-blocking)
        while (runtime.milliseconds() < time) {
            // The loop will continue until the specified time has passed
        }
    }

    // Autonomous op mode (automatically runs before teleop)
    public void autoOpMode() {
        //turnLeftRight(-0.5, 1); // Forward
        spoolUpDown(-0.35, 3000); // Spool up
        dumpBucket(5000); // Bucket down
        //turnLeftRight(0.5, 1); // Backward
    }

    
    // Runs (main method)
    @Override
    public void runOpMode() {
        // Initialize motors and servo
        LeftDrive = hardwareMap.get(DcMotor.class, "LeftDrive");
        RightDrive = hardwareMap.get(DcMotor.class, "RightDrive");
        fLeftDrive = hardwareMap.get(DcMotor.class, "fLeftDrive");
        fRightDrive = hardwareMap.get(DcMotor.class, "fRightDrive");
        
        spool = hardwareMap.get(DcMotor.class, "spool");
        
        bucket = hardwareMap.get(Servo.class, "bucket");
        
        Corehex1 = hardwareMap.get(DcMotor.class, "Corehex1");
        Corehex2 = hardwareMap.get(DcMotor.class, "Corehex2");
        

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Run the autonomous mode
        autoOpMode();
    }
}
