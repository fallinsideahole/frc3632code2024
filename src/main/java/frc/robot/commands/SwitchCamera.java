package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.subsystems.Camera;

/**
 * Add your docs here.
 */
public class SwitchCamera extends InstantCommand {
    Camera m_cameras;
    int cameraSelection = 1;
  /**
   * Add your docs here.
   */
  public SwitchCamera(Camera cameras, int cameraNum) {
    m_cameras = cameras;
    cameraSelection = cameraNum;
    addRequirements(m_cameras);
  }

  // Called once when the command executes
  @Override
  public void initialize() {
    m_cameras.setCamera(cameraSelection);
  }
}