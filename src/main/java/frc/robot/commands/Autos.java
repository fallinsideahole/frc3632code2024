// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Claw;
import frc.robot.subsystems.Launcher;

import frc.robot.Constants.AutonConstants;
import frc.robot.Constants.LauncherConstants;

public final class Autos {
  /** Example static factory for an autonomous command. */
  public static Command backupAuto(Drivetrain drivetrain, double startDelay) {
    return new RunCommand(() -> drivetrain.arcadeDrive(0, 0), drivetrain).withTimeout(startDelay)
              .andThen(new RunCommand(() -> drivetrain.arcadeDrive(AutonConstants.kAutonBackSpeed, 0), drivetrain).withTimeout(AutonConstants.kAutonBackTime))
              .andThen(new RunCommand(() -> drivetrain.arcadeDrive(0, 0), drivetrain));
  }

  // launch only
  public static Command shootAuto(Launcher launcher, Drivetrain drivetrain, double startDelay) {
    return new RunCommand(() -> drivetrain.arcadeDrive(0, 0), drivetrain).withTimeout(startDelay)
              .andThen(new PrepareLaunch(launcher).withTimeout(LauncherConstants.kLauncherDelay))
              .andThen(new LaunchNote(launcher).withTimeout(1))
              .andThen(new RunCommand(() -> launcher.stopIntake()));
  }

  // launch and drive
  public static Command shootDriveAuto(Launcher launcher, Drivetrain drivetrain, double startDelay, double driveDelay) {
    return new RunCommand(() -> drivetrain.arcadeDrive(0, 0), drivetrain).withTimeout(startDelay)
              .andThen(new PrepareLaunch(launcher).withTimeout(LauncherConstants.kLauncherDelay))
              .andThen(new LaunchNote(launcher).withTimeout(1))
              .andThen(new RunCommand(() -> launcher.stopIntake()).withTimeout(0.1))
              .andThen(new RunCommand(() -> drivetrain.arcadeDrive(0, 0), drivetrain).withTimeout(driveDelay))
              .andThen(new RunCommand(() -> drivetrain.arcadeDrive(AutonConstants.kAutonBackSpeed, 0), drivetrain).withTimeout(AutonConstants.kAutonBackTime))
              .andThen(new RunCommand(() -> drivetrain.arcadeDrive(0, 0), drivetrain));
  }

  // launch and drive amp side
  public static Command shootDriveTurnAuto(Launcher launcher, Drivetrain drivetrain, double startDelay, double driveDelay) {
    return new RunCommand(() -> drivetrain.arcadeDrive(0, 0), drivetrain).withTimeout(startDelay)
              .andThen(new PrepareLaunch(launcher).withTimeout(LauncherConstants.kLauncherDelay))
              .andThen(new LaunchNote(launcher).withTimeout(1))
              .andThen(new RunCommand(() -> launcher.stopIntake()).withTimeout(0.1))
              .andThen(new RunCommand(() -> drivetrain.arcadeDrive(0, 0), drivetrain).withTimeout(driveDelay))
              .andThen(new RunCommand(() -> drivetrain.arcadeDrive(AutonConstants.kAutonBackSpeed, 0), drivetrain).withTimeout(1))
              .andThen(new RunCommand(() -> drivetrain.arcadeDrive(0, -AutonConstants.kAutonBackSpeed), drivetrain).withTimeout(1))
              .andThen(new RunCommand(() -> drivetrain.arcadeDrive(AutonConstants.kAutonBackSpeed, 0), drivetrain).withTimeout(AutonConstants.kAutonBackTime - 0.5))
              .andThen(new RunCommand(() -> drivetrain.arcadeDrive(0, 0), drivetrain));
  }

  public static Command shootAmpAuto(Claw m_claw) {
    return new RunCommand(() -> m_claw.clawOut()).withTimeout(1);
  }


  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}
