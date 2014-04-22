//--------节日函数--------//

//y年m月的第n个星期day是几月几号
function nDayDate(y,m,n,day,calType){
  var firstDay=Day(y,m,1,calType);
  var d;
  if(day>=firstDay){
    d=(n-1)*7+day-firstDay+1;
  }
  else{
    d=n*7+day-firstDay+1;
  }
  
  m=(m<10)?'0'+m:''+m;
  d=(d<10)?'0'+d:d;
  
  return m+d;
}

//y年m月的倒数第n个星期day是几月几号
function rnDayDate(y,m,n,day,calType){
  var lastDate;
  if(y<12){
    lastDate=D0(y,m+1,1,calType)-D0(y,m,1,calType);
  }
  else{
    lastDate=31;  
  }  
  var lastDay=Day(y,m,lastDate,calType);
  
  var d;
  if(day<=lastDay){
    d=lastDate-lastDay-(n-1)*7+day;
  }
  else{
    d=lastDate-lastDay-n*7+day;
  }
  
  m=(m<10)?'0'+m:''+m;
  d=(d<10)?'0'+d:d;
  
  return m+d;
}

//alert(nDayDate(2003,5,1,6,1));
//alert(rnDayDate(2003,6,1,0,1));

//公历节日
function sFtvl(y,m,d,calType){
var sFtv=new Array(
"0101元旦",
"0214情人节",
"0305学雷锋活动日",
"0308国际妇女节",
"0312植树节",
"0315消费者权益日",
"0401愚人节",
"0501国际劳动节",
"0504五四青年节",
"0601国际儿童节",
"0701中国共产党建党日 香港回归纪念日",
"0801八一建军节",
"0808中国男子节(爸爸节)",
"0910中国教师节",
"1001国庆节",
"1220澳门回归纪念日",
"1224平安夜",
"1225圣诞节");

//某月的第几个星期几的节日
var len0=sFtv.length;
//sFtv[len0]=nDayDate(y,1,1,0,calType)+"黑人日";
//sFtv[len0+1]=rnDayDate(y,1,1,0,calType)+"国际麻风节";
//sFtv[len0+2]=rnDayDate(y,3,1,1,calType)+"中小学生安全教育日";
//sFtv[len0+3]=rnDayDate(y,4,1,3,calType)+"秘书节";
//sFtv[len0+4]=nDayDate(y,5,2,0,calType)+"国际母亲节 救助贫困母亲日";
//sFtv[len0+5]=nDayDate(y,5,3,0,calType)+"全国助残日";
//sFtv[len0+6]=nDayDate(y,5,3,2,calType)+"国际牛奶日";
//sFtv[len0+7]=nDayDate(y,6,3,0,calType)+"国际父亲节";
//sFtv[len0+8]=nDayDate(y,9,3,2,calType)+"国际和平日";
//sFtv[len0+9]=nDayDate(y,9,3,6,calType)+"全民国防教育日";
//sFtv[len0+10]=nDayDate(y,9,4,0,calType)+"国际聋人节";
//sFtv[len0+11]=rnDayDate(y,9,1,0,calType)+"世界心脏日 世界海事日";
//sFtv[len0+12]=nDayDate(y,10,1,1,calType)+"国际住房日";
//sFtv[len0+13]=nDayDate(y,10,1,3,calType)+"国际减轻自然灾害日";
sFtv[len0+14]=nDayDate(y,11,4,4,calType)+"感恩节";
//sFtv[len0+15]=nDayDate(y,12,2,0,calType)+"国际儿童电视广播日";


var str=''; //公历节日

for(i in sFtv){
   if(parseFloat(sFtv[i].substring(0,4))==100*m+d)
      str+=sFtv[i].substring(4,100);
}
  return str;
}

//农历节日
function lunFtvl(lunM,lunD){
var lunFtv=new Array(
"0101新年",
"0115元宵节",
"0505端午节",
"0523泼水节（阿昌族）",
"0707七夕",
"0715中元节",
"0815中秋节",
"0909重阳节",
"1208腊八节",
"1224小年",
"1230除夕")

var str=''; //农历节日

for(i in lunFtv){
   if(parseFloat(lunFtv[i].substring(0,4))==100*lunM+lunD)
      str+=lunFtv[i].substring(4,100);
}
  return str;
}

//节气节日
function jqFtvl(y,m,d,calType){
  var jqFtv=new Array();
  for(var i=1;i<=24;i++){
    var ind=(i<10)?('0'+i):i;
    jqFtv[i]=ind+''+sStr(i);
  }

  addName=new Array('','','','','','','节','',' 麦饭日','','','','','','','','','','','','','','','');

  var str=''; //节气节日
  var thisD0=D0(y,m,d,calType);

  for(i in jqFtv){
    if(floor(S(y,parseFloat(jqFtv[i].substring(0,2)),1,calType))==thisD0)
      str+=jqFtv[i].substring(2,100)+addName[i-1];
  }
  
  //梅雨
  var dG=gan(dGz(y,m,d,0,calType));
  var dZ=zhi(dGz(y,m,d,0,calType));

  var s11=floor(S(y,11,1,calType));
  if(thisD0>=s11&&thisD0<s11+10&&dG==3)
    str+=' 入梅';
  var s13=floor(S(y,13,1,calType));
  if(thisD0>=s13&&thisD0<s13+12&&dZ==8)
    str+=' 出梅';

  //三伏
  var s12=floor(S(y,12,1,calType));
  var s15=floor(S(y,15,1,calType));
  var n=(dG-7)%10+1;
  if(n<=0)
    n+=10;
  var firsrD0=thisD0-n+1;
//  if(firsrD0>=s12+20&&firsrD0<s12+30)
//    str+=' 初伏第'+n+'天';
//  if(firsrD0>=s15&&firsrD0<s15+10)
//    str+=' 末伏第'+n+'天';
//  else {
//  if(firsrD0>=s12+30&&firsrD0<s12+40)
//    str+=' 中伏第'+n+'天';
//  if(firsrD0>=s12+40&&firsrD0<s12+50)
//    str+=' 中伏第'+(n+10)+'天';
//  }

  //九九
//  var s24=floor(S(y,24,1,calType));
//  var s_24=floor(S(y-1,24,1,calType));
//  var d1=thisD0-s24;
//  var d2=thisD0-s_24+D0(y-1,12,31,calType)-D0(y-1,1,0,calType);
//  if(d1>=0||d2<=80){
//    if(m==12){
//      w=1;
//      v=d1+1;
//      if(v>9){
//        w+=1;
//        v-=9;
//      }
//    }
//    else{
//      var w=floor(d2/9)+1;
//      var v=round(rem(d2,9))+1;
//    }
//  str+=' '+lunDStr(w).charAt(1)+'九'+'第'+v+'天';
//  }

  return str;
}

//名人纪念日
function manFtvl(m,d){
var manFtv=new Array(
/*"0104雅各布·格林诞辰",
"0108周恩来逝世纪念日",
"0106圣女贞德诞辰",
"0112杰克·伦敦诞辰",
"0115莫里哀诞辰",
"0117富兰克林诞辰",
"0119瓦特诞辰",
"0122培根诞辰",
"0123郎之万诞辰",
"0127莫扎特诞辰",
"0129罗曼·罗兰诞辰",
"0130甘地诞辰",
"0131舒柏特诞辰",
"0203门德尔松诞辰",
"0207门捷列夫诞辰",
"0211爱迪生诞辰，狄更斯诞辰",
"0212林肯，达尔文诞辰",
"0217布鲁诺诞辰",
"0218伏打诞辰",
"0219哥白尼诞辰",
"0222赫兹，叔本华，华盛顿诞辰",
"0226雨果诞辰",
"0302斯美塔那诞辰",
"0304白求恩诞辰",
"0305周恩来诞辰",
"0306布朗宁，米开朗琪罗诞辰",
"0307竺可桢诞辰",
"0314爱因斯坦诞辰",
"0321巴赫，穆索尔斯基诞辰",
"0322贺龙诞辰",
"0328高尔基诞辰",
"0401海顿，果戈理诞辰",
"0415达·芬奇诞辰",
"0416卓别林诞辰",
"0420祖冲之诞辰",
"0422列宁，康德，奥本海默诞辰",
"0423普朗克，莎士比亚诞辰",
"0430高斯诞辰",
"0505马克思诞辰",
"0507柴可夫斯基，泰戈尔诞辰",
"0511冼星海诞辰",
"0511李比希诞辰",
"0520巴尔扎克诞辰",
"0522瓦格纳诞辰",
"0531惠特曼诞辰",
"0601杜威诞辰",
"0602哈代诞辰",
"0608舒曼诞辰",
"0715伦勃朗诞辰",
"0805阿贝尔诞辰",
"0808狄拉克诞辰",
"0826陈毅诞辰",
"0828歌德诞辰",
"0909毛泽东逝世纪念日",
"0925鲁迅诞辰",
"0926巴甫洛夫诞辰",
"0928孔子诞辰",
"0929奥斯特洛夫斯基诞辰",
"1011伯辽兹诞辰",
"1021诺贝尔诞辰",
"1022李斯特诞辰",
"1026伽罗瓦诞辰",
"1029李大钊诞辰",
"1007居里夫人诞辰",
"1108哈雷诞辰",
"1112孙中山诞辰",
"1124刘少奇诞辰",
"1128恩格斯诞辰",
"1201朱德诞辰",
"1205海森堡诞辰",
"1211玻恩诞辰",
"1213海涅诞辰",
"1216贝多芬诞辰",
"1221斯大林诞辰",
"1225牛顿诞辰",
"1226毛泽东诞辰",
"1229阿·托尔斯泰诞辰"*/);

var str=''; //名人纪念日

for(i in manFtv){
   if(parseFloat(manFtv[i].substring(0,4))==100*m+d)
      str+=manFtv[i].substring(4,100);
}
  return str;
}

