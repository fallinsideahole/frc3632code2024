package frc.robot.subsystems;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSink;
import edu.wpi.first.cscore.VideoSource.ConnectionStrategy;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Camera extends SubsystemBase {

    UsbCamera camera1;
    UsbCamera camera2;
    VideoSink server;

    public Camera() {

    camera1 = CameraServer.startAutomaticCapture(0);
    camera2 = CameraServer.startAutomaticCapture(1);
    server = CameraServer.getServer();

    camera1.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
    camera2.setConnectionStrategy(ConnectionStrategy.kKeepOpen);

    server.setSource(camera1);

    }

    public void setCamera (int cameraNumber) {
      System.out.print("Setting camera ");
      System.out.println(cameraNumber);
      if(cameraNumber == 2) {
        server.setSource(camera2);
      } else {
        server.setSource(camera1);
      }

    }
    
}

