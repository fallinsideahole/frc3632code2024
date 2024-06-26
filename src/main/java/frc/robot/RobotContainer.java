// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

import frc.robot.Constants.*;
import frc.robot.commands.Autos;
import frc.robot.commands.LaunchNote;
import frc.robot.commands.PrepareLaunch;
import frc.robot.commands.SpitNote;
import frc.robot.commands.SwitchCamera;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Launcher;
import frc.robot.subsystems.Claw;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Camera;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems are defined here.
  private final Drivetrain m_drivetrain = new Drivetrain();
  private final Launcher m_launcher = new Launcher();
  private final Claw m_claw = new Claw();
  private final Climber m_climber = new Climber();
  private final Camera m_cameras = new Camera();

  /*The gamepad provided in the KOP shows up like an XBox controller if the mode switch is set to X mode using the
   * switch on the top.*/
  private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);
  private final CommandXboxController m_operatorController =
      new CommandXboxController(OperatorConstants.kOperatorControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be accessed via the
   * named factory methods in the Command* classes in edu.wpi.first.wpilibj2.command.button (shown
   * below) or via the Trigger constructor for arbitary conditions
   */
  private void configureBindings() {

    // Set the default command for the drivetrain to drive using the joysticks
    m_drivetrain.setDefaultCommand(
        new RunCommand(() -> m_drivetrain.arcadeDrive(-m_driverController.getLeftY(), -m_driverController.getRightX()), m_drivetrain)
    );

    //alternate commands for the two lower speed levels
    m_driverController.leftBumper()
                        .or(m_driverController.rightBumper())
                        .onTrue(new RunCommand(() -> m_drivetrain.arcadeDrive(-m_driverController.getLeftY() * OperatorConstants.kMidSpeed, -m_driverController.getRightX() * OperatorConstants.kMidSpeed), m_drivetrain));
    m_driverController.leftBumper()
                        .or(m_driverController.rightBumper())
                        .onFalse(new RunCommand(() -> m_drivetrain.arcadeDrive(-m_driverController.getLeftY(), -m_driverController.getRightX()), m_drivetrain));

                        
    m_driverController.leftBumper()
                        .and(m_driverController.rightBumper())
                        .onTrue(new RunCommand(() -> m_drivetrain.arcadeDrive(-m_driverController.getLeftY() * OperatorConstants.kLowSpeed, -m_driverController.getRightX() * OperatorConstants.kLowSpeed), m_drivetrain));
    m_driverController.leftBumper()
                        .and(m_driverController.rightBumper())
                        .onFalse(new RunCommand(() -> m_drivetrain.arcadeDrive(-m_driverController.getLeftY(), -m_driverController.getRightX()), m_drivetrain));


    m_driverController.leftTrigger(0.1).onTrue(new SwitchCamera(m_cameras, 1));
    m_driverController.rightTrigger(0.1).onTrue(new SwitchCamera(m_cameras, 2));

    //this command makes the launcher a "one button" launcher where you simply hold down A and after one second it fires
    //an alternate version where the launcher is spun up when a is held and fires when right bumper is pressed is shown below

    /*Create an inline sequence to run when the operator presses and holds the A (green) button. Run the PrepareLaunch
     * command for 1 seconds and then run the LaunchNote command */
    /*m_operatorController.a().whileTrue(new PrepareLaunch(m_launcher)
                                            .withTimeout(LauncherConstants.kLauncherDelay)
                                            .andThen(new LaunchNote(m_launcher))
                                            .handleInterrupt(() -> m_launcher.stop())
                                        );*/

    //"two button" style launcher where the launcher wheel can be spun up at any point and firing is manually controlled
    //If the launcher wheel is not sufficiently spun up, the fire command will still run and your shot will fall short!

    m_operatorController.a().onTrue(new PrepareLaunch(m_launcher));
    m_operatorController.a().onFalse(m_launcher.stopIntake());

    m_operatorController.rightBumper().whileTrue(new LaunchNote(m_launcher));


    // Set up a binding to run the intake command while the operator is pressing and holding the
    // left Bumper
    m_operatorController.leftBumper().whileTrue(m_launcher.getIntakeCommand());

    // Set up a binding to spit out the note while the operator is pressing and holding b
    m_operatorController.b().onTrue(new SpitNote(m_launcher));

    // Bind intaking and exgesting with the roller claw to X and Y respectively
    m_operatorController.x().whileTrue(m_claw.clawIn());
    m_operatorController.y().whileTrue(m_claw.clawOut());


    // Bind climbing up and down to POV 0 and 180 respectively
    m_operatorController.povUp().whileTrue(m_climber.climberUp());
    m_operatorController.povDown().whileTrue(m_climber.climberDown());

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand(String autoMode, double startDelay, double driveDelay) {
    // An example command will be run in autonomous
    Command autoCommand = null;

    switch (autoMode) {
      case "do nothing":
        autoCommand = null;
      break;
      case "launch note and drive":
        autoCommand = Autos.shootDriveAuto(m_launcher, m_drivetrain, startDelay, driveDelay);
        break;
        case "launch note and drive turn":
        autoCommand = Autos.shootDriveTurnAuto(m_launcher, m_drivetrain, startDelay, driveDelay);
        break;
      case "launch":
        autoCommand = Autos.shootAuto(m_launcher,m_drivetrain, startDelay);
        break;
      case "drive":
        autoCommand = Autos.backupAuto(m_drivetrain, startDelay);
        break;
    }

    return autoCommand;
    // return Autos.backupAuto(m_drivetrain);
  }
}