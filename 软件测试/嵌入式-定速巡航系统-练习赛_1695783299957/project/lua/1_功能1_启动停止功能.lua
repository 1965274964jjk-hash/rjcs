-- 程序：setting

-- lua程序入口函数
function entry()
  --打开被测件
	local val = ask("yesno", {title='', msg='请确认被测件是否处于停止状态', default=true})
	check(val, "初始状态为停止状态","初始状态为打开状态")

	write_msg(channels.setting, protocols.P_setting,{dataValue=0})
	local val1 = ask("yesno", {title='提示', msg='请确认被测件是否处于开启状态', default=true})
	check(val1, "执行打开指令后，状态为打开状态", "执行打开指令后，状态为停止状态")

	write_msg(channels.setting, protocols.P_setting,{dataValue=0xFFFF})
	local val2 = ask("yesno", {title='提示', msg='请确认被测件是否处于停止状态', default=true})
	check(val2, "执行关闭指令后，状态为停止状态", "执行打开指令后，状态为打开状态")

	exit()
end
