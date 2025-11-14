-- 程序：gasControl

--计算电压函数
local function getOutV(Vk, Vk1, Vk2, Uk1)
	local Dp = 0.05
  local Di = 0.1
	local Dd = 0.1
	local Vd = 80
	local ek = (Vd - Vk)
	local ek1 = (Vd - Vk1)
	local ek2 = (Vd - Vk2)
	local Uk = Dp * ( ek - ek1) + Di * ek + Dd * (ek - 2 * ek1 + ek2) + Uk1
	return Uk
end

function entry()
	-- 加速到80（设定速度）
	for i=1,80 do
	  write_msg(channels.setting, protocols.P_setting,{cmdType = 2, dataValue=0})
		etimer.delay(100) 
	end
	--计算电压
	read_buff(channels.gas,0,100)
	local V1 = 60 --当前车速
	local V2 = 60 --上次车速
	local V3 = 60 --上上次车速
	local OUk1 = 1 --上次输出电压
	local OUk = 1  --当前输出电压
  for i = 1,#test_data.test_gas do
    V3 = V2
    V2 = V1
    V1 = test_data.test_gas[i].speed
    OUk1 = OUk
    OUk = getOutV(V1,V2,V3, OUk1)
    
    -- 新增：校验电压截断逻辑（负值→0）
    local expected_OUk = OUk < 0 and 0 or OUk
    print ("预期电压值为"..expected_OUk)
    
    local res = write_msg(channels.Speed, protocols.P_speed,{speed=V1})
    etimer.delay(1000)
    local resl = read_msg(channels.gas, protocols.P_gas,10000)
    local actual_voltage = resl.value.voltage < 0 and 0 or resl.value.voltage
    
    print ("实际电压值为"..actual_voltage)
    -- 新增：断言截断后电压正确性
    check(actual_voltage == expected_OUk, 
          "车速为"..V1.."时，电压"..(expected_OUk == 0 and "截断为0正确" or "计算正确"), 
          "车速为"..V1.."时，电压"..(expected_OUk == 0 and "未截断为0错误" or "计算错误"))
    print ("----------------")
  end

	exit()
end

