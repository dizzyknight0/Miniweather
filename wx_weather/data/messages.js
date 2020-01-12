let messages = () => {
  let hour = new Date().getHours()
  switch (hour) {
    case 6:
    case 7:
      return '早上好，明天和意外，不知道哪个先到，要好好珍惜今天哟'
    case 8:
    case 9:
    case 10:
    case 11:
      return '再坚持一下就下课了，是时候考虑一下中午吃什么了，软微食堂吃腻了就出去常常别的吧'
    case 12:
    case 13:
      return '下午再困也要坚持哟'
    case 14:
    case 15:
    case 16:
    case 17:
      return '下午好，下课了出去运动一下吧，为祖国健康工作五十年'
    case 18:
    case 19:
      return '冬天晚上会变冷，多穿点别生病'
    case 20:
    case 21:
      return '九点了，在休息和继续写作业的边缘疯狂挣扎'
    case 22:
    case 23:
      return '问我见过晚上十一二点的软微没？没有，我都是凌晨一两点出自习室'
    case 0:
    case 1:
      return '赶紧睡觉吧，发际线要紧'
    case 2:
    case 3:
      return '怎么失眠了呢'
    case 4:
    case 5:
      return '好了，软微起床第一人'
  }
}
module.exports = {
  messages,
}