const db = require('../lib/db');

module.exports = {
    getLeaveTypes: async () => {
        return await db.query("SELECT * FROM leave_type");
    },

    getLeaveType: async (id) => {
        return await db.query("SELECT * FROM leave_type WHERE id=?", [id]);
    },

    createLeaveType: async (name) => {
        return await db.query("INSERT INTO leave_type (name) VALUE (?)", [name]);
    },

    updateLeaveType: async (id, name) => {
        return await db.query("UPDATE leave_type SET name=? WHERE id=?", [name, id]);
    },

    deleteLeaveType: async (id) => {
        return await db.query("DELETE FROM leave_type WHERE id=?", [id]);
    },

    createUserLeaveBalance: async (json) => {
        return await db.query(
            "INSERT INTO leave_balance (leave_type_id, total_days, days_left, employee_id) VALUE (?, ?, ?, ?)", 
            [json.leaveTypeId, json.totalDays, json.totalDays, json.employeeId]
        );
    },

    getUserLeaveBalance: async (employeeId) => {
        return await db.query(
            "SELECT vs.employee_id, vs.leave_type_id, vt.name, vs.total_days, vs.days_left FROM leave_balance vs, leave_type vt WHERE vs.employee_id=? AND vs.leave_type_id=vt.id", 
            [employeeId]
        );
    },

    getUserLeaveBalanceByLeaveType: async (employeeId, leaveTypeId) => {
        return await db.query(
            "SELECT vs.employee_id, vs.leave_type_id, vt.name, vs.total_days, vs.days_left FROM leave_balance vs, leave_type vt WHERE vs.employee_id=? AND vs.leave_type_id=vt.id AND vs.leave_type_id=?", 
            [employeeId, leaveTypeId]
        );
    },

    updateUserLeaveBalance: async (employeeId, leaveTypeId, days) => {
        return await db.query("UPDATE leave_balance SET days_left=? WHERE employee_id=? AND leave_type_id=?", [days, employeeId, leaveTypeId]);
    },

    updateUserLeaveTotalDays: async (employeeId, leaveTypeId, totalDays) => {
        return await db.query("UPDATE leave_balance SET total_days=? WHERE employee_id=? AND leave_type_id=?", [totalDays, employeeId, leaveTypeId]);
    },

    resetUserLeaveBalance: async (employeeId) => {
        return await db.query("UPDATE leave_balance SET days_left=total_days WHERE employee_id=?", [employeeId]);
    },

    resetAllUserLeaveBalance: async () => {
        return await db.query("UPDATE leave_balance SET days_left=total_days");
    },

    createLeaveApplication: async (json) => {
        return await db.query("INSERT INTO leave_application (apply_date, start_date, end_date, days, leave_type_id, reason, employee_id) VALUE (?, ?, ?, ?, ?, ?, ?)",
        [json.applyDate, json.startDate, json.endDate, json.days, json.leaveTypeId, json.reason, json.employeeId]);
    },

    getLeaveApplications: async () => {
        return await db.query("SELECT va.*, vt.name FROM leave_application va, leave_type vt WHERE va.leave_type_id=vt.id");
    },

    getLeaveApplicationsWithIgnoreId: async (ignoredId) => {
        return await db.query("SELECT va.*, vt.name FROM leave_application va, leave_type vt WHERE va.leave_type_id=vt.id AND va.employee_id!=?", [ignoredId]);
    },

    getLeaveApplicationsById: async (id) => {
        return await db.query("SELECT va.*, vt.name FROM leave_application va, leave_type vt WHERE va.leave_type_id=vt.id AND va.id=?", [id]);
    },

    getLeaveApplicationsByStatus: async (status) => {
        return await db.query("SELECT * FROM leave_application WHERE status=?", [status]);
    },

    getLeaveApplicationsByEmployeeId: async (employeeId) => {
        return await db.query("SELECT va.*, vt.name FROM leave_application va, leave_type vt WHERE va.employee_id=? AND va.leave_type_id=vt.id", [employeeId]);
    },

    updateLeaveApplication: async (json) => {
        return await db.query("UPDATE leave_application SET start_date=?, end_date=?, days=?, leave_type_id=?, reason=? WHERE id=?",
        [json.startDate, json.endDate, json.days, json.leaveTypeId, json.reason, json.id]);
    },

    updateLeaveApplicationStatus: async (id, status) => {
        return await db.query("UPDATE leave_application SET status=? WHERE id=?", [status, id]);
    },

    createLeaveApprovalRecord: async (json) => {
        return await db.query("INSERT INTO leave_approval_record (leave_application_id, approver_id, status, approval_date, reason) VALUE (?, ?, ?, ?, ?)",
        [json.leaveApplicationId, json.approverId, json.status, json.approvalDate, json.reason]);
    }
}