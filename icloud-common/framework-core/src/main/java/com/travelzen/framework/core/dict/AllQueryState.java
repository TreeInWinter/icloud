/**
 * 订单查询页面上的全部状态
 * Description: 
 * Copyright (c) 2013
 * Company:真旅网
 * @author renshui
 * @version 1.0
 * @date 2013-9-23
 */
package com.travelzen.framework.core.dict;


public enum AllQueryState {
	booked("预订"),
	submitted("已提交"),
	processing("处理中"),
	processed("已处理"),
	returned("已退回"),
	;
	private String desc;
	private AllQueryState(String desc) {
        this.desc = desc;
    }
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
//	public static class GsonTypeAdapter<T> extends TypeAdapter<T> {
//
//        public void write(JsonWriter out, T value) throws IOException {
//             if (value == null) {
//                  out.nullValue();
//                  return;
//             }
//             AllQueryState status = (AllQueryState) value;
//             // Here write what you want to the JsonWriter. 
//             out.beginObject();
//             out.name("name");
//             out.value(status.name());
//             out.name("desc");
//             out.value(status.getDesc());
//             out.endObject();
//        }
//
//        public T read(JsonReader in) throws IOException {
//             // Properly deserialize the input (if you use deserialization)
//             return null;
//        }
//   }
}
