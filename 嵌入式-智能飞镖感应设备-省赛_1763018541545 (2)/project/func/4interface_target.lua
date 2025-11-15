
function Target()
  log("开始测试靶体接口容错处理")
  
  log("测试案例1: 发送包头错误的数据包")
  write_msg(channels.COM_target, protocols.target, {header = 0x5511, zone = 1})
  etimer.delay(1000)
  local res1 = ask("yesno", {
    title = '提示',
    msg = '发送了包头错误(0x5511)的数据包，请确认是否收到了靶体信息?',
    default = false
  })
  check(not res1, "包头错误有丢包处理，正确", "包头错误没有丢包处理，错误")
  etimer.delay(500)
  
  log("测试案例2: 发送数据标志1错误的数据包")
  write_msg(channels.COM_target, protocols.target, {data1 = 0xBB, zone = 2})
  etimer.delay(1000)
  local res2 = ask("yesno", {
    title = '提示',
    msg = '发送了数据标志1错误(0xBB)的数据包，请确认是否收到了靶体信息?',
    default = false
  })
  check(not res2, "数据标志1错误有丢包处理，正确", "数据标志1错误没有丢包处理，错误")
  etimer.delay(500)
  
  log("测试案例3: 发送数据标志2错误的数据包")
  write_msg(channels.COM_target, protocols.target, {data2 = 0x22, zone = 3})
  etimer.delay(1000)
  local res3 = ask("yesno", {
    title = '提示',
    msg = '发送了数据标志2错误(0x22)的数据包，请确认是否收到了靶体信息?',
    default = false
  })
  check(not res3, "数据标志2错误有丢包处理，正确", "数据标志2错误没有丢包处理，错误")
  etimer.delay(500)
  
  log("测试案例4: 发送包尾错误的数据包")
  write_msg(channels.COM_target, protocols.target, {tail = 0xAA55, zone = 4})
  etimer.delay(1000)
  local res4 = ask("yesno", {
    title = '提示',
    msg = '发送了包尾错误(0xAA55)的数据包，请确认是否收到了靶体信息?',
    default = false
  })
  check(not res4, "包尾错误有丢包处理，正确", "包尾错误没有丢包处理，错误")
  etimer.delay(500)
  
  log("测试案例5: 发送校验和错误的数据包")
  write_msg(channels.COM_target, protocols.target, {check = 0xFF, zone = 5})
  etimer.delay(1000)
  local res5 = ask("yesno", {
    title = '提示',
    msg = '发送了校验和错误(0xFF)的数据包，请确认是否收到了靶体信息?',
    default = false
  })
  check(not res5, "校验和错误有丢包处理，正确", "校验和错误没有丢包处理，错误")
  etimer.delay(500)
  
  log("测试案例6: 发送投中区域值错误(无效值0)的数据包")
  write_msg(channels.COM_target, protocols.target, {zone = 0})
  etimer.delay(1000)
  local res6 = ask("yesno", {
    title = '提示',
    msg = '发送了投中区域值错误(0)的数据包，请确认是否收到了靶体信息?',
    default = false
  })
  check(not res6, "投中区域值错误有丢包处理，正确", "投中区域值错误没有丢包处理，错误")
  etimer.delay(500)
  
  log("测试案例7: 发送投中区域值错误(无效值6)的数据包")
  write_msg(channels.COM_target, protocols.target, {zone = 6})
  etimer.delay(1000)
  local res7 = ask("yesno", {
    title = '提示',
    msg = '发送了投中区域值错误(6)的数据包，请确认是否收到了靶体信息?',
    default = false
  })
  check(not res7, "投中区域值超出范围有丢包处理，正确", "投中区域值超出范围没有丢包处理，错误")
  etimer.delay(500)
  
  log("测试案例8: 发送完全正确的数据包(内牛眼区域)")
  write_msg(channels.COM_target, protocols.target, {zone = 1})
  etimer.delay(1000)
  local res8 = ask("yesno", {
    title = '提示',
    msg = '发送了正确的数据包(内牛眼区域，区域值1)，请确认是否收到了靶体信息?',
    default = true
  })
  check(res8, "正确数据包(内牛眼区域)接收成功，正确", "正确数据包(内牛眼区域)未接收，错误")
  etimer.delay(500)
  
  log("测试案例9: 发送完全正确的数据包(三倍区域)")
  write_msg(channels.COM_target, protocols.target, {zone = 3})
  etimer.delay(1000)
  local res9 = ask("yesno", {
    title = '提示',
    msg = '发送了正确的数据包(三倍区域，区域值3)，请确认是否收到了靶体信息?',
    default = true
  })
  check(res9, "正确数据包(三倍区域)接收成功，正确", "正确数据包(三倍区域)未接收，错误")
  etimer.delay(500)
  
  log("测试案例10: 发送多字段错误的数据包")
  write_msg(channels.COM_target, protocols.target, {header = 0x1234, data1 = 0xAA, data2 = 0xBB, tail = 0x9999, zone = 5})
  etimer.delay(1000)
  local res10 = ask("yesno", {
    title = '提示',
    msg = '发送了包头、数据标志1、数据标志2、包尾都错误的数据包，请确认是否收到了靶体信息?',
    default = false
  })
  check(not res10, "多字段错误有丢包处理，正确", "多字段错误没有丢包处理，错误")
  etimer.delay(500)
  
  log("测试案例11: 发送完全正确的数据包验证系统恢复(单倍区域)")
  write_msg(channels.COM_target, protocols.target, {zone = 5})
  etimer.delay(1000)
  local res11 = ask("yesno", {
    title = '提示',
    msg = '发送了正确的数据包(单倍区域，区域值5)，请确认是否收到了靶体信息?',
    default = true
  })
  check(res11, "错误数据包后系统恢复正常，正确", "错误数据包后系统未恢复正常，错误")
  
  log("靶体接口容错测试完成")
end

function entry()
  Target()
  exit()
end
