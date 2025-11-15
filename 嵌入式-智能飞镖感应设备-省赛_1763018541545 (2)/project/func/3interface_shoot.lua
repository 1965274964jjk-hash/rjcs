
function Shoot()
  log("开始测试数据传送机接口容错处理")
  
  log("测试案例1: 发送包头错误的数据包")
  write_msg(channels.COM_shoot, protocols.shoot, {header = 0x5511, type = 1, number = 10})
  etimer.delay(1000)
  local res1 = ask("yesno", {
    title = '提示',
    msg = '发送了包头错误(0x5511)的数据包，请确认是否收到了飞镖信息?',
    default = false
  })
  check(not res1, "包头错误有丢包处理，正确", "包头错误没有丢包处理，错误")
  etimer.delay(500)
  
  log("测试案例2: 发送数据标志错误的数据包")
  write_msg(channels.COM_shoot, protocols.shoot, {data = 0xCC, type = 1, number = 10})
  etimer.delay(1000)
  local res2 = ask("yesno", {
    title = '提示',
    msg = '发送了数据标志错误(0xCC)的数据包，请确认是否收到了飞镖信息?',
    default = false
  })
  check(not res2, "数据标志错误有丢包处理，正确", "数据标志错误没有丢包处理，错误")
  etimer.delay(500)
  
  log("测试案例3: 发送包尾错误的数据包")
  write_msg(channels.COM_shoot, protocols.shoot, {tail = 0xAA55, type = 1, number = 10})
  etimer.delay(1000)
  local res3 = ask("yesno", {
    title = '提示',
    msg = '发送了包尾错误(0xAA55)的数据包，请确认是否收到了飞镖信息?',
    default = false
  })
  check(not res3, "包尾错误有丢包处理，正确", "包尾错误没有丢包处理，错误")
  etimer.delay(500)
  
  log("测试案例4: 发送校验和错误的数据包")
  write_msg(channels.COM_shoot, protocols.shoot, {check = 0xFF, type = 1, number = 10})
  etimer.delay(1000)
  local res4 = ask("yesno", {
    title = '提示',
    msg = '发送了校验和错误(0xFF)的数据包，请确认是否收到了飞镖信息?',
    default = false
  })
  check(not res4, "校验和错误有丢包处理，正确", "校验和错误没有丢包处理，错误")
  etimer.delay(500)
  
  log("测试案例5: 发送完全正确的数据包")
  write_msg(channels.COM_shoot, protocols.shoot, {type = 1, number = 10})
  etimer.delay(1000)
  local res5 = ask("yesno", {
    title = '提示',
    msg = '发送了正确的数据包(轻镖，数量10)，请确认是否收到了飞镖信息?',
    default = true
  })
  check(res5, "正确数据包接收成功，正确", "正确数据包未接收，错误")
  etimer.delay(500)
  
  log("测试案例6: 发送多字段错误的数据包")
  write_msg(channels.COM_shoot, protocols.shoot, {header = 0x1234, data = 0xAA, tail = 0x9999, type = 2, number = 5})
  etimer.delay(1000)
  local res6 = ask("yesno", {
    title = '提示',
    msg = '发送了包头、数据标志、包尾都错误的数据包，请确认是否收到了飞镖信息?',
    default = false
  })
  check(not res6, "多字段错误有丢包处理，正确", "多字段错误没有丢包处理，错误")
  etimer.delay(500)
  
  log("测试案例7: 发送包头正确但其他字段错误的数据包")
  write_msg(channels.COM_shoot, protocols.shoot, {data = 0xAA, tail = 0x1234, type = 1, number = 5})
  etimer.delay(1000)
  local res7 = ask("yesno", {
    title = '提示',
    msg = '发送了包头正确但数据标志和包尾错误的数据包，请确认是否收到了飞镖信息?',
    default = false
  })
  check(not res7, "部分字段错误有丢包处理，正确", "部分字段错误没有丢包处理，错误")
  etimer.delay(500)
  
  log("测试案例8: 再次发送完全正确的数据包验证系统恢复")
  write_msg(channels.COM_shoot, protocols.shoot, {type = 2, number = 15})
  etimer.delay(1000)
  local res8 = ask("yesno", {
    title = '提示',
    msg = '发送了正确的数据包(重镖，数量15)，请确认是否收到了飞镖信息?',
    default = true
  })
  check(res8, "错误数据包后系统恢复正常，正确", "错误数据包后系统未恢复正常，错误")
  
  log("数据传送机接口容错测试完成")
end

function entry()
  Shoot()
  exit()
end
