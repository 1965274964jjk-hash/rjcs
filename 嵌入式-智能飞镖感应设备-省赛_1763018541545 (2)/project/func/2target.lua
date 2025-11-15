
function Target()
  log("开始测试靶区信息接收功能")
  
  for _index, value in ipairs(test_data.target) do
    local zone = value.zone
    
    log(string.format("测试案例 %d: 投中区域=%d", _index, zone))
    
    write_msg(channels.COM_target, protocols.target, {zone = zone})
    etimer.delay(1000)
    
    local zone_str = ""
    if zone == 1 then
      zone_str = "内牛眼区域"
    elseif zone == 2 then
      zone_str = "外牛眼区域"
    elseif zone == 3 then
      zone_str = "三倍区域"
    elseif zone == 4 then
      zone_str = "双倍区域"
    elseif zone == 5 then
      zone_str = "单倍区域"
    else
      zone_str = "无效区域"
    end
    
    if zone >= 1 and zone <= 5 then
      local res = ask("yesno", {
        title = '确认',
        msg = string.format('请确认是否收到投中区域：%s(区域值:%d)', zone_str, zone),
        default = false
      })
      check(res,
        string.format("成功接收有效靶区信息 - %s(区域值:%d)", zone_str, zone),
        string.format("未能接收有效靶区信息 - %s(区域值:%d)", zone_str, zone))
    else
      local res = ask("yesno", {
        title = '确认',
        msg = string.format('投中区域值无效(区域值:%d)，请确认系统是否做了异常处理?', zone),
        default = false
      })
      check(res,
        string.format("无效区域值%d，系统正确处理", zone),
        string.format("无效区域值%d，系统未正确处理", zone))
    end
    
    etimer.delay(500)
  end
  
  log("靶区信息接收功能测试完成")
end

function entry()
  Target()
  exit()
end
