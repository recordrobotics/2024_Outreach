// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.manual;
import frc.robot.control.AbstractControl;
import frc.robot.shuffleboard.ShuffleboardUI;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.Command;

/** An example command that uses an example subsystem. */
public class ManualShooter extends Command {

  @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

  // Creates Drivetrain and Controls variables
  private Shooter _shooter;
  public AbstractControl _controls;

  /**
   * @param drivetrain
   */
  public ManualDrive(Shooter shooter) {
    // Init variables
    _shooter = shooter;
    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    AbstractControl _controls = ShuffleboardUI.Overview.getControl();

    ShooterStates shooterCommandData = _controls.getShooter();
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