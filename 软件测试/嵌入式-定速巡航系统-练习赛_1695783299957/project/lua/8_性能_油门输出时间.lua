-- 程序：perf_time

-- lua程序入口函数
function entry()
	-- 此处输入程序代码
	local sumTime = 0
	for i=1,10 do
	  local start = etimer.now()
	  local res = write_msg(channels.Speed, protocols.P_speed,{speed=50})
    read_buff(channels.gas, 0, 5000)
	  local endT = etimer.now()
	  print (endT-start)
	  sumTime = sumTime + (endT-start)
    end
	local eveTime = sumTime/10.0
	print ("平均响应时间是"..eveTime)
	exit()
end
