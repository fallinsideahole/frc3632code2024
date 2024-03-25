// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

  /*
   * Autonomous selection options.
   */
  private static final String kNothingAuto = "do nothing";
  private static final String kLaunchAndDrive = "launch note and drive";
  private static final String kLaunchAndDriveTurn = "launch note and drive turn";
  private static final String kLaunch = "launch";
  private static final String kDrive = "drive";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private double autoStartDelay;
  private double autoDriveDelay;


  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {

    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();

    m_chooser.setDefaultOption("do nothing", kNothingAuto);
    m_chooser.addOption("launch note and drive", kLaunchAndDrive);
    m_chooser.addOption("launch note , turn, and drive", kLaunchAndDriveTurn);
    m_chooser.addOption("launch", kLaunch);
    m_chooser.addOption("drive", kDrive);
    SmartDashboard.putData("Auto choices", m_chooser);
    SmartDashboard.putNumber("autoStartDelay", 0);
    SmartDashboard.putNumber("autoDriveDelay", 1);
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test modes.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
    SmartDashboard.putNumber("Time (seconds)", Timer.getFPGATimestamp());
  }

  @Override
  public void autonomousInit() {

    m_autoSelected = m_chooser.getSelected();

    autoStartDelay = SmartDashboard.getNumber("autoStartDelay", 0);
    autoDriveDelay =SmartDashboard.getNumber("autoDriveDelay", 1);

    System.out.println(m_autoSelected);

    m_autonomousCommand = m_robotContainer.getAutonomousCommand(m_autoSelected, autoStartDelay, autoDriveDelay);

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }

  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {

  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

}

@Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

}