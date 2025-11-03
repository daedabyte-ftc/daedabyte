package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "DpadMovement2 (Blocks to Java)")
public class DpadMovement2 extends LinearOpMode {

  private DcMotor front_right_drive;
  private DcMotor back_right_drive;
  private DcMotor back_left_drive;
  private DcMotor front_left_drive;

  /**
   * This OpMode offers POV (point-of-view) style TeleOp control for a direct drive robot.
   *
   * In this POV mode, the left joystick (up and down) moves the robot forward and back, and the
   * right joystick (left and right) spins the robot left (counterclockwise) and right (clockwise).
   */
  @Override
  public void runOpMode() {
    front_right_drive = hardwareMap.get(DcMotor.class, "front_right_drive");
    back_right_drive = hardwareMap.get(DcMotor.class, "back_right_drive");
    back_left_drive = hardwareMap.get(DcMotor.class, "back_left_drive");
    front_left_drive = hardwareMap.get(DcMotor.class, "front_left_drive");

    front_right_drive.setDirection(DcMotor.Direction.REVERSE);
    back_right_drive.setDirection(DcMotor.Direction.REVERSE);
    back_left_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    back_right_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    front_left_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    front_right_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    waitForStart();
    if (opModeIsActive()) {
      while (opModeIsActive()) {
        // Use left stick to drive and right stick to turn
        // The Y axis of a joystick ranges from -1 in its topmost position to +1 in its bottommost position.
        // We negate this value so that the topmost position corresponds to maximum forward power.
        back_left_drive.setPower(1 * (gamepad1.left_stick_y + gamepad1.left_stick_x));
        back_right_drive.setPower(1 * (gamepad1.left_stick_y - gamepad1.left_stick_x));
        front_left_drive.setPower(1 * (gamepad1.left_stick_y - gamepad1.left_stick_x));
        front_right_drive.setPower(1 * (gamepad1.left_stick_y + gamepad1.left_stick_x));
        // The Y axis of a joystick ranges from -1 in its topmost position to +1 in its bottommost position.
        // We negate this value so that the topmost position corresponds to maximum forward power.
        back_left_drive.setPower(-1 * gamepad1.right_stick_x);
        back_right_drive.setPower(1 * gamepad1.right_stick_x);
        // The Y axis of a joystick ranges from -1 in its topmost position to +1 in its bottommost position.
        // We negate this value so that the topmost position corresponds to maximum forward power.
        front_left_drive.setPower(-1 * gamepad1.right_stick_x);
        front_right_drive.setPower(1 * gamepad1.right_stick_x);
      }
    }
  }
}
