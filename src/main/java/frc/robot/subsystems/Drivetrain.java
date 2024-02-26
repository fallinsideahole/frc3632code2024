// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.DrivetrainConstants.*;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.filter.SlewRateLimiter;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/* This class declares the subsystem for the robot drivetrain if controllers are connected via CAN. Make sure to go to
 * RobotContainer and uncomment the line declaring this subsystem and comment the line for PWMDrivetrain.
 *
 * The subsystem contains the objects for the hardware contained in the mechanism and handles low level logic
 * for control. Subsystems are a mechanism that, when used in conjuction with command "Requirements", ensure
 * that hardware is only being used by 1 command at a time.
 */
public class Drivetrain extends SubsystemBase {
  /*Class member variables. These variables represent things the class needs to keep track of and use between
  different method calls. */
  DifferentialDrive m_drivetrain;
  

  /*Slew rate limiter 
     *This will "soften" sudden movements of the control sticks
     *Remove these lines or experiment with the numbers if you do not like the effect
     *The number represents how far across its total range of speed outputs the robot can change in one second
     *a value of 0.8 means the robot can go from 0 to 0.8 of max in one second (or from 0.4 to -0.4, etc)
     *a value of 2.5 means the robot can go from 0 to 2.5 its max speed in one second
     *what this really means is the robot can go from 0 to top speed (1) in 0.4 seconds
     *(or -0.5 to 0.5 in 0.4 seconds, or 0.7 to -0.3 in 0.4 seconds, or 1 to -1 in 0.8 seconds, etc)
    */
  SlewRateLimiter filter = new SlewRateLimiter(2.5);
  SlewRateLimiter turnFilter = new SlewRateLimiter(2.5);


  /*Constructor. This method is called when an instance of the class is created. This should generally be used to set up
   * member variables and perform any configuration or set up necessary on hardware.
   */
  public Drivetrain() {

    /*
     * Drive motor controller instances.
     *
     * Change the id's to match your robot.
     * Change kBrushed to kBrushless if you are uisng NEOs.
     * The rookie kit comes with CIMs which are brushed motors.
     * Use the appropriate other class if you are using different controllers.
     */
    CANSparkBase leftRear = new CANSparkMax(kLeftRearID, MotorType.kBrushless);
    CANSparkBase leftFront = new CANSparkMax(kLeftFrontID, MotorType.kBrushless);
    CANSparkBase rightRear = new CANSparkMax(kRightRearID, MotorType.kBrushless);
    CANSparkBase rightFront = new CANSparkMax(kRightFrontID, MotorType.kBrushless);

    /*Sets current limits for the drivetrain motors. This helps reduce the likelihood of wheel spin, reduces motor heating
     *at stall (Drivetrain pushing against something) and helps maintain battery voltage under heavy demand */
    leftFront.setSmartCurrentLimit(kCurrentLimit);
    leftRear.setSmartCurrentLimit(kCurrentLimit);
    rightFront.setSmartCurrentLimit(kCurrentLimit);
    rightRear.setSmartCurrentLimit(kCurrentLimit);

    /*
     * Motors can be set to idle in brake or coast mode.
     *
     * Brake mode effectively shorts the leads of the motor when not running, making it more
     * difficult to turn when not running.
     *
     * Coast doesn't apply any brake and allows the motor to spin down naturally with the robot's momentum.
     *
     * (touch the leads of a motor together and then spin the shaft with your fingers to feel the difference)
     *
     * This setting is driver preference. Try setting the idle modes below to kBrake to see the difference.
     */
    /*
    leftRear.setIdleMode(IdleMode.kCoast);
    leftFront.setIdleMode(IdleMode.kCoast);
    rightRear.setIdleMode(IdleMode.kCoast);
    rightFront.setIdleMode(IdleMode.kCoast);
    */

    // Set the rear motors to follow the front motors.
    leftRear.follow(leftFront);
    rightRear.follow(rightFront);

    // Invert one side so both side drive forward with positive motor outputs
    leftFront.setInverted(false);
    rightFront.setInverted(true);

    // Put the front motors into the differential drive object. This will control all 4 motors with
    // the rears set to follow the fronts
    m_drivetrain = new DifferentialDrive(leftFront, rightFront);
  
  }

  /*Method to control the drivetrain using arcade drive. Arcade drive takes a speed in the X (forward/back) direction
   * and a rotation about the Z (turning the robot about it's center) and uses these to control the drivetrain motors */
  public void arcadeDrive(double speed, double rotation) {

    m_drivetrain.arcadeDrive(filter.calculate(speed), turnFilter.calculate(rotation));

    //uncomment this line and comment out the line above if you want to disable the slew rate
    //m_drivetrain.arcadeDrive(speed, rotation);
  }

  /*Method for autonomous without the slew rate filter so it does not introduce noise into intended effect
   *autonomous can be run with the filters by calling the regular arcadeDrive method in Autos.java
  */
  public void autonArcadeDrive(double speed, double rotation) {
    m_drivetrain.arcadeDrive(speed, rotation);
  }

  @Override
  public void periodic() {
    /*This method will be called once per scheduler run. It can be used for running tasks we know we want to update each
     * loop such as processing sensor data. Our drivetrain is simple so we don't have anything to put here */
  }
}
