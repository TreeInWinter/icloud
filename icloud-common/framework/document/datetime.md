之前的szframework_core里的 DateUtil和TimeUtil 乱糟糟的，而且大量使用了臭名昭著的 jdk自带的时间日期函数。



我基于  joda-time 重写了 DateUtil和TimeUtil几乎所有时间日期相关函数，

 
DateUtil和TimeUtil 将在 libra系统正式上线之后删除。

任何情况下， 请不要在libra系统中使用 jdk中的 Date, TimeZone 对象和函数。


如需添加新的日期时间函数，  增加到 DateTimeUtil中，  发 code-review给全组 .

如果是 ibatis里自动生成的 Date类型， 在自己的代码转型一下，再调用 DateTimeUtil里的方法.


或者在 DateTimeUtil对应的方法里， 加一个多态方法，  以Date为参数的那个方法，实际调用以DateTime为参数的方法:

比如

        /**
         * 将日期对象转为指定格式日期字符串
         * 
         * @param date
         * @param intFormat
         * @return
         */
        public static String format(DateTime date, String format) {
                DateTimeFormatter sdf = DateTimeFormat.forPattern(format);
                return sdf.print(date);
        }

        
        public static String format(Date date, String format) {
                return format(new DateTime( date),   format);
        }
        
        
        
