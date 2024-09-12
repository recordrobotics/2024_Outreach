// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.manual;
import frc.robot.control.AbstractControl;
import frc.robot.shuffleboard.ShuffleboardUI;
import frc.robot.subsystems.Channel;
import edu.wpi.first.wpilibj2.command.Command;

/** An example command that uses an example subsystem. */
public class ManualChannel extends Command {

  @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

  // Creates Drivetrain and Controls variables
  private Channel _channel;
  public AbstractControl _controls;

  /**
   * @param drivetrain
   */
  public ManualDrive(Channel channel) {
    // Init variables
    _channel = channel;
    addRequirements(channel);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    AbstractControl _controls = ShuffleboardUI.Overview.getControl();

    ChannelStates shooterCommandData = _controls.getShooter();
    _shooter.toggle(shooterCommandData);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}