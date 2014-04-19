
package com.icloud.framework.core.dict;

import java.util.HashMap;
import java.util.Map;

public enum GatheringRecordState {

    req_frozen("请求冻结"),
    frozen_success("冻结成功"),
    frozen_fail("冻结失败"),
    req_unfrozen_causedBy_reject("拒绝出票引发的请求解冻"),
    unfrozen_success_causedBy_reject("拒绝出票引发的解冻成功"),
    unfrozen_fail_causedBy_reject("拒绝出票引发的解冻失败"),
    unfrozen_causedBy_cancel("取消订单引发的解冻"),
    gathering_success("收银成功"),
    gathering_fail("收银失败"),
    req_gathering("请求收银"),
    req_settlement("请求结算"),
    req_settlement_fail("请求结算失败"),
    settlement_fail("结算失败"),
    settlement_success("结算成功");
   

    public static Map<GatheringRecordState, FlightOrderItemMajorStageState> majorStageMappingtTable = new HashMap<GatheringRecordState, FlightOrderItemMajorStageState>();

    private String desc;

    static{
        majorStageMappingtTable.put(req_frozen, FlightOrderItemMajorStageState.not_begin);
        majorStageMappingtTable.put(frozen_success, FlightOrderItemMajorStageState.not_begin);
        majorStageMappingtTable.put(frozen_fail, FlightOrderItemMajorStageState.not_begin);
        majorStageMappingtTable.put(req_unfrozen_causedBy_reject, FlightOrderItemMajorStageState.not_begin);
        majorStageMappingtTable.put(unfrozen_success_causedBy_reject, FlightOrderItemMajorStageState.not_begin);
        majorStageMappingtTable.put(unfrozen_fail_causedBy_reject, FlightOrderItemMajorStageState.not_begin);
        majorStageMappingtTable.put(gathering_success, FlightOrderItemMajorStageState.complete);
        majorStageMappingtTable.put(gathering_fail, FlightOrderItemMajorStageState.exception);
        majorStageMappingtTable.put(req_gathering, FlightOrderItemMajorStageState.processing);
    }

    private GatheringRecordState(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
