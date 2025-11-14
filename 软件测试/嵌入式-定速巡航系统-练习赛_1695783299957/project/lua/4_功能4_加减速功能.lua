-- 程序：settingSpeedUp

-- lua程序入口函数
function entry()
	--设定速度为0
	for i=1,80 do
	  write_msg(channels.setting, protocols.P_setting,{cmdType = 2, dataValue=0xFFFF})
		etimer.delay(100) 
	end
  local val0 = ask("yesno", {title='提示', msg='请确认被测件设定速度是为0', default=true})
  
  -- 加速
	write_msg(channels.setting, protocols.P_setting,{cmdType = 2, dataValue=0})
	local val1 = ask("yesno", {title='提示', msg='请确认被测件设定速度是否增加了1', default=true})
	check(val1, "加速指令正常", "加速指令错误")
  --读取油门通道数据
  local res = read_msg(channels.gas, protocols.P_gas, 1000)
  check(res~=nil, "开启状态下加速有输出油门数据，正确", "开启状态下加速没输出油门数据，错误")
  -- 减速
	write_msg(channels.setting, protocols.P_setting,{cmdType = 2, dataValue=0xFFFF})
	local val2 = ask("yesno", {title='提示', msg='请确认被测件设定速度是否减少了1', default=true})
	check(val2, "减速指令正常", "减速执行错误")
  --读取油门通道数据
  local res = read_msg(channels.gas, protocols.P_gas, 1000)
  check(res~=nil, "开启状态下减速有输出油门数据，正确", "开启状态下减速没输出油门数据，错误")

	-- 停止
  read_buff(channels.gas,0 , 1000)
	write_msg(channels.setting, protocols.P_setting,{cmdType = 1, dataValue=0xFFFF})--停止
  etimer.delay(100)
  -- 加速
  write_msg(channels.setting, protocols.P_setting,{cmdType = 2, dataValue=0})--加速
  etimer.delay(100)
  local val3 = ask("yesno", {title='请确定', msg='停止状态设定速度未加1', default=true})
  check(val3, "停止状态未加速，正确", "停止状态加速，错误")
  -- 减速
  write_msg(channels.setting, protocols.P_setting,{cmdType = 2, dataValue=0xFFFF})--减速
  etimer.delay(100)
  local val4 = ask("yesno", {title='请确定', msg='停止状态设定速度未减1', default=true})
  check(val4, "停止状态未减速，正确", "停止状态减速，错误")
  --读取油门通道数据
	local res = read_msg(channels.gas, protocols.P_gas, 1000)
	check(res==nil, "停止状态下加减速没有输出油门数据，正确", "停止状态下加减速输出油门数据，错误") 
  
  --开启，设定速度小于0？
  write_msg(channels.setting, protocols.P_setting,{cmdType = 1, dataValue=0})--开启
  etimer.delay(100)
  write_msg(channels.setting, protocols.P_setting,{cmdType = 2, dataValue=0xFFFF})--减速
	etimer.delay(100)
	local val3 = ask("yesno", {title='提示', msg='请确认被测件设定速度是否少于0', default=true})
  check(not val3, "设定速度下限为0正确", "设定速度下限为0错误")
  
  --开启，设定速大于100？
  for i=1,105 do
	  write_msg(channels.setting, protocols.P_setting,{cmdType = 2, dataValue=0})
		etimer.delay(100)
	end
	local val4 = ask("yesno", {title='提示', msg='请确认被测件设定速度是否超过了100', default=true})
	check(not val4, "设定速度上限为100正确", "设定速度上限为100错误")

  -- 新增：连续加速超边界测试（99→100→100，第2次加速不生效）
write_msg(channels.setting, protocols.P_setting,{cmdType = 1, dataValue=0})--开启
etimer.delay(100)
-- 先加速到99
for i=1,99 do
  write_msg(channels.setting, protocols.P_setting,{cmdType = 2, dataValue=0})
  etimer.delay(50)
end
local val5 = ask("yesno", {title='提示', msg='请确认被测件设定速度为99', default=true})
check(val5, "连续加速到99正确", "连续加速到99错误")
-- 第1次加速到100（生效）
write_msg(channels.setting, protocols.P_setting,{cmdType = 2, dataValue=0})
etimer.delay(50)
local val6 = ask("yesno", {title='提示', msg='请确认被测件设定速度为100', default=true})
check(val6, "99→100加速生效正确", "99→100加速生效错误")
-- 第2次加速到101（不生效）
write_msg(channels.setting, protocols.P_setting,{cmdType = 2, dataValue=0})
etimer.delay(50)
local val7 = ask("yesno", {title='提示', msg='请确认被测件设定速度仍为100', default=true})
check(val7, "100→101加速不生效正确", "100→101加速不生效错误")

-- 新增：边界值恢复测试（100→99，减速生效）
write_msg(channels.setting, protocols.P_setting,{cmdType = 2, dataValue=0xFFFF})
etimer.delay(50)
local val8 = ask("yesno", {title='提示', msg='请确认被测件设定速度为99', default=true})
check(val8, "100→99减速生效正确", "100→99减速生效错误")

-- 新增：连续减速超边界测试（1→0→0，第2次减速不生效）
-- 先减速到1
for i=1,98 do
  write_msg(channels.setting, protocols.P_setting,{cmdType = 2, dataValue=0xFFFF})
  etimer.delay(50)
end
local val9 = ask("yesno", {title='提示', msg='请确认被测件设定速度为1', default=true})
check(val9, "连续减速到1正确", "连续减速到1错误")
-- 第1次减速到0（生效）
write_msg(channels.setting, protocols.P_setting,{cmdType = 2, dataValue=0xFFFF})
etimer.delay(50)
local val10 = ask("yesno", {title='提示', msg='请确认被测件设定速度为0', default=true})
check(val10, "1→0减速生效正确", "1→0减速生效错误")
-- 第2次减速到-1（不生效）
write_msg(channels.setting, protocols.P_setting,{cmdType = 2, dataValue=0xFFFF})
etimer.delay(50)
local val11 = ask("yesno", {title='提示', msg='请确认被测件设定速度仍为0', default=true})
check(val11, "0→-1减速不生效正确", "0→-1减速不生效错误")


  exit()
end
