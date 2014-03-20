package com.travelzen.framework.core.dict;


public enum FlowType {
     all("全部"),
     review("审核"),
     issue("出票"),
    ;
    private String desc;
    private FlowType(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
//    public static class GsonTypeAdapter<T> extends TypeAdapter<T> {
//
//        public void write(JsonWriter out, T value) throws IOException {
//             if (value == null) {
//                  out.nullValue();
//                  return;
//             }
//             FlowType status = (FlowType) value;
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
