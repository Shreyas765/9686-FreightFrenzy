package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.hardware.RevIMU;
import com.arcrobotics.ftclib.hardware.SimpleServo;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.ArcadeDriveCommmand;
import org.firstinspires.ftc.teamcode.commands.DuckySpinnerBlueCommand;
import org.firstinspires.ftc.teamcode.commands.DuckySpinnerRedCommand;
import org.firstinspires.ftc.teamcode.commands.IntakeCommand;
import org.firstinspires.ftc.teamcode.commands.IntakeDoorCommand;
import org.firstinspires.ftc.teamcode.commands.OuttakeCommand;
import org.firstinspires.ftc.teamcode.commands.SlowArcadeDriveCommand;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.DuckySpinnerSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;

@TeleOp(name = "MainTeleOp-CB")
public class MainTeleOp extends CommandOpMode {

    private Motor fL, bL, fR, bR;
    private MotorGroupTemp leftDrive, rightDrive;

    private Motor duckySpinner;
    private Motor intake;

    private SimpleServo intakeDoor;

    private GamepadEx gPad1;
    private RevIMU imu;

    private DriveSubsystem driveSubsystem;
    private DuckySpinnerSubsystem duckySpinnerSubsystem;
    private IntakeSubsystem intakeSubsystem;

    private ArcadeDriveCommmand arcadeDriveCommmand;
    private SlowArcadeDriveCommand slowArcadeDriveCommand;

    private DuckySpinnerBlueCommand duckySpinnerBlueCommand;
    private DuckySpinnerRedCommand duckySpinnerRedCommand;

    private IntakeCommand intakeCommand;
    private IntakeDoorCommand intakeDoorCommand;
    private OuttakeCommand outtakeCommand;

    @Override
    public void initialize() {

        fL = new Motor(hardwareMap, "frontLeft");
        fR = new Motor(hardwareMap, "frontRight");
        bL = new Motor(hardwareMap, "backLeft");
        bR = new Motor(hardwareMap, "backRight");

        duckySpinner = new Motor(hardwareMap, "duckySpinner");
        intake = new Motor(hardwareMap, "intake");

        intakeDoor = new SimpleServo(hardwareMap, "intakeDoor", 0, 180);

        leftDrive = new MotorGroupTemp(fL, bL);
        rightDrive = new MotorGroupTemp(fR, bR);

        gPad1 = new GamepadEx(gamepad1);
        imu = new RevIMU(hardwareMap);
        imu.init();

        driveSubsystem = new DriveSubsystem(leftDrive, rightDrive, imu);
        arcadeDriveCommmand = new ArcadeDriveCommmand(driveSubsystem, gPad1::getLeftY, gPad1::getRightX);
        slowArcadeDriveCommand = new SlowArcadeDriveCommand(driveSubsystem, gPad1::getLeftY, gPad1::getRightX, 0.33);

        duckySpinnerSubsystem = new DuckySpinnerSubsystem(duckySpinner);
        duckySpinnerBlueCommand = new DuckySpinnerBlueCommand(duckySpinnerSubsystem);
        duckySpinnerRedCommand = new DuckySpinnerRedCommand(duckySpinnerSubsystem);

        intakeSubsystem = new IntakeSubsystem(intake, intakeDoor);
        intakeCommand = new IntakeCommand(intakeSubsystem);
        intakeDoorCommand = new IntakeDoorCommand(intakeSubsystem);
        outtakeCommand = new OuttakeCommand(intakeSubsystem);

        gPad1.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenHeld(slowArcadeDriveCommand);

        gPad1.getGamepadButton(GamepadKeys.Button.X).whenHeld(duckySpinnerBlueCommand);
        gPad1.getGamepadButton(GamepadKeys.Button.B).whenHeld(duckySpinnerRedCommand);

        gPad1.getGamepadButton(GamepadKeys.Button.Y).whenHeld(intakeCommand);
        gPad1.getGamepadButton(GamepadKeys.Button.A).whenPressed(intakeDoorCommand);
        gPad1.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenHeld(outtakeCommand);

        register(driveSubsystem);
        driveSubsystem.setDefaultCommand(arcadeDriveCommmand);

    }
}
