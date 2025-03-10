package ch.cern.eam.wshub.core.services.equipment.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(EquipmentPMSchedulePK.class)
@Table(name="R5PPMOBJECTS")
@NamedQuery(name=EquipmentPMSchedule.FIND_PM_SCHEDULE, query="SELECT pmschedule FROM EquipmentPMSchedule pmschedule WHERE pmschedule.equipmentCode = :equipmentCode AND (pmschedule.pmCode = :pmCode OR :pmCode IS NULL)")
public class EquipmentPMSchedule implements Serializable {

	public static final String FIND_PM_SCHEDULE = "FIND_PM_SCHEDULE";
	@Id
	@Column(name="PPO_PK")
	private String sequenceNumber;
	@Id
	@Column(name="PPO_REVISION")
	private String revision;
	@Column(name="PPO_OBJECT")
	private String equipmentCode;
	@Column(name="PPO_PPM")
	private String pmCode;
	@Transient private String departmentCode;
	@Transient private String periodLength;
	@Transient private String periodUOM;
	@Transient private String dueDate;
	@Transient private String Route;
	@Transient private String scheduleType;
	@Transient private String changed;
	@Transient private String supervisor;
	@Transient private String costCode;
	@Transient private String meter1Interval;
	@Transient private String meter1UOM;
	@Transient private String meter1Due;
	@Transient private String meter2Interval;
	@Transient private String meter2UOM;
	@Transient private String meter2Due;
	@Transient private String dateDeactivated;
	@Transient private String location;
	@Transient private String workOrder;

	public String getWorkOrderClass() {
		return workOrderClass;
	}

	public void setWorkOrderClass(String workOrderClass) {
		this.workOrderClass = workOrderClass;
	}

	@Transient private String workOrderClass;
	@Transient private String assignedTo;
	
	public String getEquipmentCode() {
		return equipmentCode;
	}
	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}
	public String getPmCode() {
		return pmCode;
	}
	public void setPmCode(String pmCode) {
		this.pmCode = pmCode;
	}
	public String getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}
	public String getPeriodLength() {
		return periodLength;
	}
	public void setPeriodLength(String periodLength) {
		this.periodLength = periodLength;
	}
	public String getPeriodUOM() {
		return periodUOM;
	}
	public void setPeriodUOM(String periodUOM) {
		this.periodUOM = periodUOM;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getRoute() {
		return Route;
	}
	public void setRoute(String route) {
		Route = route;
	}
	public String getScheduleType() {
		return scheduleType;
	}
	public void setScheduleType(String scheduleType) {
		this.scheduleType = scheduleType;
	}
	
	public String getChanged() {
		return changed;
	}
	public void setChanged(String changed) {
		this.changed = changed;
	}
	public String getSupervisor() {
		return supervisor;
	}
	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}
	public String getCostCode() {
		return costCode;
	}
	public void setCostCode(String costCode) {
		this.costCode = costCode;
	}
	public String getMeter1Interval() {
		return meter1Interval;
	}
	public void setMeter1Interval(String meter1Interval) {
		this.meter1Interval = meter1Interval;
	}
	public String getMeter1UOM() {
		return meter1UOM;
	}
	public void setMeter1UOM(String meter1uom) {
		meter1UOM = meter1uom;
	}
	public String getMeter1Due() {
		return meter1Due;
	}
	public void setMeter1Due(String meter1Due) {
		this.meter1Due = meter1Due;
	}
	public String getMeter2Interval() {
		return meter2Interval;
	}
	public void setMeter2Interval(String meter2Interval) {
		this.meter2Interval = meter2Interval;
	}
	public String getMeter2UOM() {
		return meter2UOM;
	}
	public void setMeter2UOM(String meter2uom) {
		meter2UOM = meter2uom;
	}
	public String getMeter2Due() {
		return meter2Due;
	}
	public void setMeter2Due(String meter2Due) {
		this.meter2Due = meter2Due;
	}
	public String getDateDeactivated() {
		return dateDeactivated;
	}
	public void setDateDeactivated(String dateDeactivated) {
		this.dateDeactivated = dateDeactivated;
	}
	public String getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public String getRevision() {
		return revision;
	}
	public void setRevision(String revision) {
		this.revision = revision;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getWorkOrder() {
		return workOrder;
	}
	public void setWorkOrder(String workOrder) {
		this.workOrder = workOrder;
	}
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	@Override
	public String toString() {
		return "EquipmentPMSchedule{" +
				"sequenceNumber='" + sequenceNumber + '\'' +
				", revision='" + revision + '\'' +
				", equipmentCode='" + equipmentCode + '\'' +
				", pmCode='" + pmCode + '\'' +
				", departmentCode='" + departmentCode + '\'' +
				", periodLength='" + periodLength + '\'' +
				", periodUOM='" + periodUOM + '\'' +
				", dueDate='" + dueDate + '\'' +
				", Route='" + Route + '\'' +
				", scheduleType='" + scheduleType + '\'' +
				", changed='" + changed + '\'' +
				", supervisor='" + supervisor + '\'' +
				", costCode='" + costCode + '\'' +
				", meter1Interval='" + meter1Interval + '\'' +
				", meter1UOM='" + meter1UOM + '\'' +
				", meter1Due='" + meter1Due + '\'' +
				", meter2Interval='" + meter2Interval + '\'' +
				", meter2UOM='" + meter2UOM + '\'' +
				", meter2Due='" + meter2Due + '\'' +
				", dateDeactivated='" + dateDeactivated + '\'' +
				", location='" + location + '\'' +
				", workOrder='" + workOrder + '\'' +
				", workOrderClass='" + workOrderClass + '\'' +
				", assignedTo='" + assignedTo + '\'' +
				'}';
	}
}
