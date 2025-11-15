
function Shoot()
  log("开始测试飞镖信息输入功能")
  
  for _index, value in ipairs(test_data.shoot) do
    local type = value.type
    local num = value.number
    
    log(string.format("测试案例 %d: 飞镖类型=%d, 飞镖数量=%d", _index, type, num))
    
    write_msg(channels.COM_shoot, protocols.shoot, {type = type, number = num})
    etimer.delay(1000)
    
    local dart_type_str = ""
    if type == 1 then
      dart_type_str = "轻镖"
    elseif type == 2 then
      dart_type_str = "重镖"
    else
      dart_type_str = "未知物"
    end
    
    if type == 1 or type == 2 then
      if num >= 1 and num <= 20 then
        local res = ask("yesno", {
          title = '确认',
          msg = string.format('请确认是否收到飞镖类型：%s(%d)，飞镖数量：%d', dart_type_str, type, num),
          default = false
        })
        check(res, 
          string.format("成功接收有效飞镖信息 - 类型:%s(%d), 数量:%d", dart_type_str, type, num),
          string.format("未能接收有效飞镖信息 - 类型:%s(%d), 数量:%d", dart_type_str, type, num))
      else
        local res = ask("yesno", {
          title = '确认',
          msg = string.format('飞镖数量超出范围[1-20]，当前值:%d，请确认系统是否做了异常处理?', num),
          default = false
        })
        check(res,
          string.format("飞镖数量%d超出范围，系统正确处理", num),
          string.format("飞镖数量%d超出范围，系统未正确处理", num))
      end
    else
      local res = ask("yesno", {
        title = '确认',
        msg = string.format('飞镖类型为未知物(类型值:%d)，请确认系统是否做了异常处理?', type),
        default = false
      })
      check(res,
        string.format("未知物类型%d，系统正确处理", type),
        string.format("未知物类型%d，系统未正确处理", type))
    end
    
    etimer.delay(500)
  end
  
  log("飞镖信息输入功能测试完成")
end

function entry()
  Shoot()
  exit()
end
