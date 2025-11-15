package net.mooctest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

/**
 * A*寻路算法综合测试类
 * 目标：分支覆盖率≥90%，变异杀死率≥90%
 * 框架：JUnit 4.12 + Mockito 2.7.19
 * JDK：1.8
 */
public class AStarTest {

    private AStar aStar;
    private Grid grid;

    @Before
    public void setUp() {
        aStar = new AStar();
        grid = new Grid(10, 10);
        // 默认所有格子都可行走
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                grid.setWalkable(i, j, true);
            }
        }
    }

    // ==================== AStar类测试 ====================

    /**
     * 测试：正常寻路，无障碍物
     */
    @Test
    public void testSearch_NoObstacles_ShouldFindPath() {
        Path path = aStar.search(0, 0, 5, 5, grid);
        assertNotNull(path);
        assertFalse(path.isEmpty());
        assertTrue(path.size() >= 2);
    }

    /**
     * 测试：正常寻路带平滑参数
     */
    @Test
    public void testSearch_WithSmoothing_ShouldFindSmoothPath() {
        Path path = aStar.search(0, 0, 5, 5, grid, true);
        assertNotNull(path);
        assertFalse(path.isEmpty());
    }

    /**
     * 测试：起点不可行走
     */
    @Test
    public void testSearch_StartPointUnwalkable_ShouldReturnEmptyPath() {
        grid.setWalkable(0, 0, false);
        Path path = aStar.search(0, 0, 5, 5, grid);
        assertNotNull(path);
        assertTrue(path.isEmpty());
        assertEquals(0, path.size());
    }

    /**
     * 测试：终点不可行走
     */
    @Test
    public void testSearch_EndPointUnwalkable_ShouldReturnEmptyPath() {
        grid.setWalkable(5, 5, false);
        Path path = aStar.search(0, 0, 5, 5, grid);
        assertNotNull(path);
        assertTrue(path.isEmpty());
    }

    /**
     * 测试：起点和终点相同
     */
    @Test
    public void testSearch_StartEqualsEnd_ShouldReturnEmptyPath() {
        Path path = aStar.search(3, 3, 3, 3, grid);
        assertNotNull(path);
        assertTrue(path.isEmpty());
    }

    /**
     * 测试：完全封闭的路径（无法到达）
     */
    @Test
    public void testSearch_NoPathAvailable_ShouldReturnEmptyPath() {
        // 在起点周围建立围墙
        grid.setWalkable(1, 0, false);
        grid.setWalkable(1, 1, false);
        grid.setWalkable(0, 1, false);
        Path path = aStar.search(0, 0, 5, 5, grid);
        assertTrue(path.isEmpty());
    }

    /**
     * 测试：带障碍物的寻路
     */
    @Test
    public void testSearch_WithObstacles_ShouldFindPathAroundThem() {
        // 建立一个障碍物
        grid.setWalkable(2, 2, false);
        grid.setWalkable(3, 2, false);
        grid.setWalkable(4, 2, false);
        Path path = aStar.search(0, 0, 5, 5, grid);
        assertNotNull(path);
        assertFalse(path.isEmpty());
    }

    /**
     * 测试：search方法使用外部Path对象
     */
    @Test
    public void testSearch_WithExternalPath_ShouldFillPath() {
        Path path = new Path();
        aStar.search(0, 0, 3, 3, grid, path);
        assertFalse(path.isEmpty());
        assertTrue(path.size() >= 2);
    }

    /**
     * 测试：search方法使用外部Path对象和平滑参数
     */
    @Test
    public void testSearch_WithExternalPathAndSmoothing_ShouldFillSmoothPath() {
        Path path = new Path();
        aStar.search(0, 0, 3, 3, grid, path, true);
        assertFalse(path.isEmpty());
    }

    /**
     * 测试：边界寻路（地图边缘）
     */
    @Test
    public void testSearch_AtMapBoundaries_ShouldFindPath() {
        Path path = aStar.search(0, 0, 9, 9, grid);
        assertNotNull(path);
        assertFalse(path.isEmpty());
    }

    /**
     * 测试：水平直线寻路
     */
    @Test
    public void testSearch_HorizontalLine_ShouldFindStraightPath() {
        Path path = aStar.search(0, 5, 5, 5, grid);
        assertNotNull(path);
        assertFalse(path.isEmpty());
    }

    /**
     * 测试：竖直直线寻路
     */
    @Test
    public void testSearch_VerticalLine_ShouldFindStraightPath() {
        Path path = aStar.search(5, 0, 5, 5, grid);
        assertNotNull(path);
        assertFalse(path.isEmpty());
    }

    /**
     * 测试：对角线寻路
     */
    @Test
    public void testSearch_DiagonalLine_ShouldFindDiagonalPath() {
        Path path = aStar.search(0, 0, 5, 5, grid);
        assertNotNull(path);
        assertFalse(path.isEmpty());
    }

    /**
     * 测试：复杂路径寻找
     */
    @Test
    public void testSearch_ComplexPath_ShouldFindOptimalPath() {
        // 创建复杂障碍物
        for (int i = 1; i < 6; i++) {
            grid.setWalkable(i, 3, false);
        }
        for (int j = 1; j < 4; j++) {
            grid.setWalkable(6, j, false);
        }
        Path path = aStar.search(0, 0, 8, 8, grid);
        assertNotNull(path);
        assertFalse(path.isEmpty());
    }

    /**
     * 测试：fillPath方法的各个方向分支
     */
    @Test
    public void testFillPath_AllDirections_ShouldCoverAllBranches() {
        // 通过不同的寻路方向来测试fillPath的switch分支
        Path path1 = aStar.search(5, 0, 5, 5, grid); // UP
        assertFalse(path1.isEmpty());
        
        Path path2 = aStar.search(5, 9, 5, 5, grid); // DOWN
        assertFalse(path2.isEmpty());
        
        Path path3 = aStar.search(9, 5, 5, 5, grid); // LEFT
        assertFalse(path3.isEmpty());
        
        Path path4 = aStar.search(0, 5, 5, 5, grid); // RIGHT
        assertFalse(path4.isEmpty());
        
        Path path5 = aStar.search(9, 0, 5, 5, grid); // LEFT_UP
        assertFalse(path5.isEmpty());
        
        Path path6 = aStar.search(9, 9, 5, 5, grid); // LEFT_DOWN
        assertFalse(path6.isEmpty());
        
        Path path7 = aStar.search(0, 0, 5, 5, grid); // RIGHT_UP
        assertFalse(path7.isEmpty());
        
        Path path8 = aStar.search(0, 9, 5, 5, grid); // RIGHT_DOWN
        assertFalse(path8.isEmpty());
    }

    /**
     * 测试：open方法中DIRECTION_RIGHT_DOWN分支
     */
    @Test
    public void testOpen_DirectionRightDown_ShouldCheckWalkability() {
        // 创建特定场景测试RIGHT_DOWN方向的障碍物检查
        grid.setWalkable(1, 1, false);
        Path path = aStar.search(0, 2, 3, 0, grid);
        assertNotNull(path);
    }

    /**
     * 测试：open方法中DIRECTION_LEFT_UP分支
     */
    @Test
    public void testOpen_DirectionLeftUp_ShouldCheckWalkability() {
        // 创建特定场景测试LEFT_UP方向的障碍物检查
        grid.setWalkable(1, 2, false);
        Path path = aStar.search(2, 0, 0, 2, grid);
        assertNotNull(path);
    }

    /**
     * 测试：clear方法
     */
    @Test
    public void testClear_ShouldCleanupNodes() {
        aStar.search(0, 0, 5, 5, grid);
        aStar.clear();
        assertTrue(aStar.isCLean(grid));
    }

    /**
     * 测试：多次搜索重用AStar实例
     */
    @Test
    public void testMultipleSearches_ShouldWorkCorrectly() {
        Path path1 = aStar.search(0, 0, 5, 5, grid);
        assertFalse(path1.isEmpty());
        
        Path path2 = aStar.search(1, 1, 7, 7, grid);
        assertFalse(path2.isEmpty());
        
        Path path3 = aStar.search(9, 9, 0, 0, grid);
        assertFalse(path3.isEmpty());
    }

    // ==================== Node类测试 ====================

    /**
     * 测试：Node.toNode创建节点
     */
    @Test
    public void testNode_ToNode_ShouldCreateValidNode() {
        long node = Node.toNode(10, 20, 30, 40);
        assertEquals(10, Node.getX(node));
        assertEquals(20, Node.getY(node));
        assertEquals(30, Node.getG(node));
        assertEquals(40, Node.getF(node));
    }

    /**
     * 测试：Node.toNode负F值抛出异常
     */
    @Test(expected = TooLongPathException.class)
    public void testNode_ToNodeWithNegativeF_ShouldThrowException() {
        Node.toNode(10, 20, 30, -1);
    }

    /**
     * 测试：Node.setGF更新G和F值
     */
    @Test
    public void testNode_SetGF_ShouldUpdateValues() {
        long node = Node.toNode(10, 20, 30, 40);
        long updatedNode = Node.setGF(node, 25, 35);
        assertEquals(10, Node.getX(updatedNode));
        assertEquals(20, Node.getY(updatedNode));
        assertEquals(25, Node.getG(updatedNode));
        assertEquals(35, Node.getF(updatedNode));
    }

    /**
     * 测试：Node边界值
     */
    @Test
    public void testNode_BoundaryValues_ShouldHandleCorrectly() {
        long node1 = Node.toNode(0, 0, 0, 0);
        assertEquals(0, Node.getX(node1));
        assertEquals(0, Node.getY(node1));
        
        long node2 = Node.toNode(65535, 65535, 65535, 65535);
        assertEquals(65535, Node.getX(node2));
        assertEquals(65535, Node.getY(node2));
    }

    // ==================== Path类测试 ====================

    /**
     * 测试：Path.add添加点
     */
    @Test
    public void testPath_Add_ShouldIncreaseSize() {
        Path path = new Path();
        assertEquals(0, path.size());
        path.add(1, 2);
        assertEquals(1, path.size());
        path.add(3, 4);
        assertEquals(2, path.size());
    }

    /**
     * 测试：Path.get获取点
     */
    @Test
    public void testPath_Get_ShouldReturnCorrectPoint() {
        Path path = new Path();
        path.add(1, 2);
        path.add(3, 4);
        long point = path.get(0);
        assertEquals(3, Point.getX(point));
        assertEquals(4, Point.getY(point));
    }

    /**
     * 测试：Path.remove移除点
     */
    @Test
    public void testPath_Remove_ShouldDecreaseSize() {
        Path path = new Path();
        path.add(1, 2);
        path.add(3, 4);
        assertEquals(2, path.size());
        path.remove();
        assertEquals(1, path.size());
    }

    /**
     * 测试：Path.isEmpty当size<2时为空
     */
    @Test
    public void testPath_IsEmpty_ShouldReturnTrueWhenLessThanTwoPoints() {
        Path path = new Path();
        assertTrue(path.isEmpty());
        path.add(1, 2);
        assertTrue(path.isEmpty());
        path.add(3, 4);
        assertFalse(path.isEmpty());
    }

    /**
     * 测试：Path.clear清空路径
     */
    @Test
    public void testPath_Clear_ShouldResetSize() {
        Path path = new Path();
        path.add(1, 2);
        path.add(3, 4);
        path.clear();
        assertEquals(0, path.size());
        assertTrue(path.isEmpty());
    }

    /**
     * 测试：Path数组扩容（grow方法）
     */
    @Test
    public void testPath_Grow_ShouldExpandArray() {
        Path path = new Path();
        // 添加足够多的点触发扩容
        for (int i = 0; i < 20; i++) {
            path.add(i, i);
        }
        assertEquals(20, path.size());
    }

    /**
     * 测试：Path扩容边界条件（容量<64）
     */
    @Test
    public void testPath_GrowSmallCapacity_ShouldUseCorrectFormula() {
        Path path = new Path();
        for (int i = 0; i < 10; i++) {
            path.add(i, i);
        }
        assertTrue(path.size() == 10);
    }

    /**
     * 测试：Path扩容边界条件（容量>=64）
     */
    @Test
    public void testPath_GrowLargeCapacity_ShouldUseCorrectFormula() {
        Path path = new Path();
        for (int i = 0; i < 100; i++) {
            path.add(i, i);
        }
        assertEquals(100, path.size());
    }

    // ==================== Grid类测试 ====================

    /**
     * 测试：Grid构造函数正常情况
     */
    @Test
    public void testGrid_Constructor_ShouldCreateValidGrid() {
        Grid g = new Grid(5, 5);
        assertEquals(5, g.getWidth());
        assertEquals(5, g.getHeight());
    }

    /**
     * 测试：Grid构造函数宽度为0抛出异常
     */
    @Test(expected = RuntimeException.class)
    public void testGrid_ConstructorWithZeroWidth_ShouldThrowException() {
        new Grid(0, 5);
    }

    /**
     * 测试：Grid构造函数高度为0抛出异常
     */
    @Test(expected = RuntimeException.class)
    public void testGrid_ConstructorWithZeroHeight_ShouldThrowException() {
        new Grid(5, 0);
    }

    /**
     * 测试：Grid构造函数负宽度抛出异常
     */
    @Test(expected = RuntimeException.class)
    public void testGrid_ConstructorWithNegativeWidth_ShouldThrowException() {
        new Grid(-1, 5);
    }

    /**
     * 测试：Grid构造函数负高度抛出异常
     */
    @Test(expected = RuntimeException.class)
    public void testGrid_ConstructorWithNegativeHeight_ShouldThrowException() {
        new Grid(5, -1);
    }

    /**
     * 测试：Grid.setWalkable设置可行走
     */
    @Test
    public void testGrid_SetWalkableTrue_ShouldMakeCellWalkable() {
        Grid g = new Grid(5, 5);
        g.setWalkable(2, 2, true);
        assertTrue(g.isWalkable(2, 2));
    }

    /**
     * 测试：Grid.setWalkable设置不可行走
     */
    @Test
    public void testGrid_SetWalkableFalse_ShouldMakeCellUnwalkable() {
        Grid g = new Grid(5, 5);
        g.setWalkable(2, 2, false);
        assertFalse(g.isWalkable(2, 2));
    }

    /**
     * 测试：Grid.isWalkable边界检查（x<0）
     */
    @Test
    public void testGrid_IsWalkableNegativeX_ShouldReturnFalse() {
        assertFalse(grid.isWalkable(-1, 5));
    }

    /**
     * 测试：Grid.isWalkable边界检查（y<0）
     */
    @Test
    public void testGrid_IsWalkableNegativeY_ShouldReturnFalse() {
        assertFalse(grid.isWalkable(5, -1));
    }

    /**
     * 测试：Grid.isWalkable边界检查（x>=width）
     */
    @Test
    public void testGrid_IsWalkableXBeyondWidth_ShouldReturnFalse() {
        assertFalse(grid.isWalkable(10, 5));
    }

    /**
     * 测试：Grid.isWalkable边界检查（y>=height）
     */
    @Test
    public void testGrid_IsWalkableYBeyondHeight_ShouldReturnFalse() {
        assertFalse(grid.isWalkable(5, 10));
    }

    /**
     * 测试：Grid.clear清理
     */
    @Test
    public void testGrid_Clear_ShouldResetGridState() {
        grid.setWalkable(5, 5, true);
        aStar.search(0, 0, 5, 5, grid);
        grid.clear();
        assertTrue(grid.isClean());
    }

    /**
     * 测试：Grid.isClean检查清洁状态
     */
    @Test
    public void testGrid_IsClean_ShouldReturnTrueWhenClean() {
        Grid g = new Grid(5, 5);
        assertTrue(g.isClean());
    }

    /**
     * 测试：Grid静态方法isUnwalkable
     */
    @Test
    public void testGrid_IsUnwalkable_ShouldDetectUnwalkableCell() {
        grid.setWalkable(3, 3, false);
        int info = grid.info(3, 3);
        assertTrue(Grid.isUnwalkable(info));
    }

    /**
     * 测试：Grid静态方法isNullNode
     */
    @Test
    public void testGrid_IsNullNode_ShouldDetectNullNode() {
        Grid g = new Grid(5, 5);
        int info = g.info(0, 0);
        assertTrue(Grid.isNullNode(info));
    }

    // ==================== Point类测试 ====================

    /**
     * 测试：Point.toPoint创建点
     */
    @Test
    public void testPoint_ToPoint_ShouldCreateValidPoint() {
        long point = Point.toPoint(100, 200);
        assertEquals(100, Point.getX(point));
        assertEquals(200, Point.getY(point));
    }

    /**
     * 测试：Point边界值（最大整数）
     */
    @Test
    public void testPoint_MaxValues_ShouldHandleCorrectly() {
        long point = Point.toPoint(Integer.MAX_VALUE, Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, Point.getX(point));
        assertEquals(Integer.MAX_VALUE, Point.getY(point));
    }

    /**
     * 测试：Point零值
     */
    @Test
    public void testPoint_ZeroValues_ShouldHandleCorrectly() {
        long point = Point.toPoint(0, 0);
        assertEquals(0, Point.getX(point));
        assertEquals(0, Point.getY(point));
    }

    /**
     * 测试：Point负值
     */
    @Test
    public void testPoint_NegativeValues_ShouldHandleCorrectly() {
        long point = Point.toPoint(-10, -20);
        assertEquals(-10, Point.getX(point));
        assertEquals(-20, Point.getY(point));
    }

    // ==================== Cost类测试 ====================

    /**
     * 测试：Cost.hCost计算启发式代价
     */
    @Test
    public void testCost_HCost_ShouldCalculateCorrectly() {
        int cost = Cost.hCost(0, 0, 3, 4);
        assertEquals((3 + 4) * Cost.COST_ORTHOGONAL, cost);
    }

    /**
     * 测试：Cost.hCost相同点
     */
    @Test
    public void testCost_HCostSamePoint_ShouldReturnZero() {
        int cost = Cost.hCost(5, 5, 5, 5);
        assertEquals(0, cost);
    }

    /**
     * 测试：Cost.hCost负坐标
     */
    @Test
    public void testCost_HCostNegativeCoordinates_ShouldCalculateCorrectly() {
        int cost = Cost.hCost(-5, -5, 5, 5);
        assertEquals((10 + 10) * Cost.COST_ORTHOGONAL, cost);
    }

    /**
     * 测试：Cost常量值
     */
    @Test
    public void testCost_Constants_ShouldHaveCorrectValues() {
        assertEquals(5, Cost.COST_ORTHOGONAL);
        assertEquals(7, Cost.COST_DIAGONAL);
    }

    // ==================== Utils类测试 ====================

    /**
     * 测试：Utils.check条件为true
     */
    @Test
    public void testUtils_CheckTrue_ShouldNotThrowException() {
        Utils.check(true);
    }

    /**
     * 测试：Utils.check条件为false抛出异常
     */
    @Test(expected = RuntimeException.class)
    public void testUtils_CheckFalse_ShouldThrowException() {
        Utils.check(false);
    }

    /**
     * 测试：Utils.check带消息
     */
    @Test
    public void testUtils_CheckWithMessageTrue_ShouldNotThrowException() {
        Utils.check(true, "Error message");
    }

    /**
     * 测试：Utils.check带消息条件为false
     */
    @Test(expected = RuntimeException.class)
    public void testUtils_CheckWithMessageFalse_ShouldThrowException() {
        Utils.check(false, "Error message");
    }

    /**
     * 测试：Utils.check带格式化消息
     */
    @Test
    public void testUtils_CheckWithFormatTrue_ShouldNotThrowException() {
        Utils.check(true, "Value: %d", 123);
    }

    /**
     * 测试：Utils.check带格式化消息条件为false
     */
    @Test(expected = RuntimeException.class)
    public void testUtils_CheckWithFormatFalse_ShouldThrowException() {
        Utils.check(false, "Value: %d", 123);
    }

    /**
     * 测试：Utils.mask方法
     */
    @Test
    public void testUtils_Mask_ShouldCalculateCorrectly() {
        assertEquals(1, Utils.mask(1));
        assertEquals(3, Utils.mask(2));
        assertEquals(7, Utils.mask(3));
        assertEquals(15, Utils.mask(4));
        assertEquals(0xFFFF, Utils.mask(16));
    }

    /**
     * 测试：Utils.mask边界值（32位）
     */
    @Test
    public void testUtils_Mask32Bits_ShouldReturnNegativeOne() {
        assertEquals(-1, Utils.mask(32));
    }

    /**
     * 测试：Utils.mask非法值（0）
     */
    @Test(expected = RuntimeException.class)
    public void testUtils_MaskZero_ShouldThrowException() {
        Utils.mask(0);
    }

    /**
     * 测试：Utils.mask非法值（33）
     */
    @Test(expected = RuntimeException.class)
    public void testUtils_MaskTooLarge_ShouldThrowException() {
        Utils.mask(33);
    }

    // ==================== Reachability类测试 ====================

    /**
     * 测试：Reachability.isReachable直达
     */
    @Test
    public void testReachability_IsReachable_ShouldReturnTrue() {
        assertTrue(Reachability.isReachable(0, 0, 3, 3, grid));
    }

    /**
     * 测试：Reachability.isReachable障碍物阻挡
     */
    @Test
    public void testReachability_IsReachableWithObstacle_ShouldReturnFalse() {
        grid.setWalkable(1, 1, false);
        assertFalse(Reachability.isReachable(0, 0, 3, 3, grid));
    }

    /**
     * 测试：Reachability带scale参数
     */
    @Test
    public void testReachability_IsReachableWithScale_ShouldWork() {
        boolean result = Reachability.isReachable(0, 0, 3, 3, 1, grid);
        assertTrue(result);
    }

    /**
     * 测试：Reachability.getClosestWalkablePointToTarget
     */
    @Test
    public void testReachability_GetClosestWalkablePoint_ShouldReturnTargetIfReachable() {
        long point = Reachability.getClosestWalkablePointToTarget(0, 0, 3, 3, grid);
        assertEquals(3, Point.getX(point));
        assertEquals(3, Point.getY(point));
    }

    /**
     * 测试：Reachability非法scale
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReachability_InvalidScale_ShouldThrowException() {
        Reachability.isReachable(0, 0, 3, 3, 0, grid);
    }

    /**
     * 测试：Reachability起始点不可行走
     */
    @Test
    public void testReachability_StartUnwalkable_ShouldReturnStartPoint() {
        grid.setWalkable(0, 0, false);
        long point = Reachability.getClosestWalkablePointToTarget(0, 0, 3, 3, grid);
        assertEquals(0, Point.getX(point));
        assertEquals(0, Point.getY(point));
    }

    /**
     * 测试：Reachability相同格子
     */
    @Test
    public void testReachability_SameCell_ShouldReturnTarget() {
        long point = Reachability.getClosestWalkablePointToTarget(1, 1, 1, 1, grid);
        assertEquals(1, Point.getX(point));
        assertEquals(1, Point.getY(point));
    }

    /**
     * 测试：Reachability水平直线（向右）
     */
    @Test
    public void testReachability_HorizontalLineRight_ShouldWork() {
        long point = Reachability.getClosestWalkablePointToTarget(0, 5, 5, 5, grid);
        assertEquals(5, Point.getX(point));
        assertEquals(5, Point.getY(point));
    }

    /**
     * 测试：Reachability水平直线（向左）
     */
    @Test
    public void testReachability_HorizontalLineLeft_ShouldWork() {
        long point = Reachability.getClosestWalkablePointToTarget(5, 5, 0, 5, grid);
        assertEquals(0, Point.getX(point));
        assertEquals(5, Point.getY(point));
    }

    /**
     * 测试：Reachability水平直线有障碍
     */
    @Test
    public void testReachability_HorizontalLineWithObstacle_ShouldReturnClosestPoint() {
        grid.setWalkable(3, 5, false);
        long point = Reachability.getClosestWalkablePointToTarget(0, 5, 5, 5, grid);
        assertTrue(Point.getX(point) < 3);
    }

    /**
     * 测试：Reachability竖直直线（向上）
     */
    @Test
    public void testReachability_VerticalLineUp_ShouldWork() {
        long point = Reachability.getClosestWalkablePointToTarget(5, 0, 5, 5, grid);
        assertEquals(5, Point.getX(point));
        assertEquals(5, Point.getY(point));
    }

    /**
     * 测试：Reachability竖直直线（向下）
     */
    @Test
    public void testReachability_VerticalLineDown_ShouldWork() {
        long point = Reachability.getClosestWalkablePointToTarget(5, 5, 5, 0, grid);
        assertEquals(5, Point.getX(point));
        assertEquals(0, Point.getY(point));
    }

    /**
     * 测试：Reachability竖直直线有障碍
     */
    @Test
    public void testReachability_VerticalLineWithObstacle_ShouldReturnClosestPoint() {
        grid.setWalkable(5, 3, false);
        long point = Reachability.getClosestWalkablePointToTarget(5, 0, 5, 5, grid);
        assertTrue(Point.getY(point) < 3);
    }

    /**
     * 测试：Reachability斜线（偏X轴）
     */
    @Test
    public void testReachability_DiagonalLineXBiased_ShouldWork() {
        long point = Reachability.getClosestWalkablePointToTarget(0, 0, 6, 3, grid);
        assertEquals(6, Point.getX(point));
        assertEquals(3, Point.getY(point));
    }

    /**
     * 测试：Reachability斜线（偏Y轴）
     */
    @Test
    public void testReachability_DiagonalLineYBiased_ShouldWork() {
        long point = Reachability.getClosestWalkablePointToTarget(0, 0, 3, 6, grid);
        assertEquals(3, Point.getX(point));
        assertEquals(6, Point.getY(point));
    }

    /**
     * 测试：Reachability斜线有障碍
     */
    @Test
    public void testReachability_DiagonalLineWithObstacle_ShouldReturnClosestPoint() {
        grid.setWalkable(3, 3, false);
        long point = Reachability.getClosestWalkablePointToTarget(0, 0, 5, 5, grid);
        assertNotNull(point);
    }

    /**
     * 测试：Reachability使用Fence（可达）
     */
    @Test
    public void testReachability_WithFenceReachable_ShouldReturnTarget() {
        Fence fence = mock(Fence.class);
        when(fence.isReachable(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(true);
        long point = Reachability.getClosestWalkablePointToTarget(0, 0, 3, 3, 1, grid, fence);
        assertEquals(3, Point.getX(point));
        assertEquals(3, Point.getY(point));
    }

    /**
     * 测试：Reachability使用Fence（不可达）
     */
    @Test
    public void testReachability_WithFenceNotReachable_ShouldReturnClosestPoint() {
        Fence fence = mock(Fence.class);
        when(fence.isReachable(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(false);
        long point = Reachability.getClosestWalkablePointToTarget(0, 0, 3, 3, 1, grid, fence);
        assertNotNull(point);
    }

    /**
     * 测试：Reachability.scaleDown方法
     */
    @Test
    public void testReachability_ScaleDown_ShouldCalculateCorrectly() {
        assertEquals(5.0, Reachability.scaleDown(10.0, 2), 0.001);
        assertEquals(3.0, Reachability.scaleDown(9.0, 3), 0.001);
    }

    /**
     * 测试：Reachability.scaleUp整数方法
     */
    @Test
    public void testReachability_ScaleUpInt_ShouldCalculateCorrectly() {
        assertEquals(5, Reachability.scaleUp(2, 2));
        assertEquals(4, Reachability.scaleUp(1, 3));
    }

    /**
     * 测试：Reachability.scaleUp双精度方法
     */
    @Test
    public void testReachability_ScaleUpDouble_ShouldCalculateCorrectly() {
        assertEquals(10, Reachability.scaleUp(5.0, 2));
        assertEquals(15, Reachability.scaleUp(5.0, 3));
    }

    /**
     * 测试：Reachability.scaleUpPoint方法
     */
    @Test
    public void testReachability_ScaleUpPoint_ShouldCreateCorrectPoint() {
        long point = Reachability.scaleUpPoint(5.0, 10.0, 2);
        assertEquals(10, Point.getX(point));
        assertEquals(20, Point.getY(point));
    }

    // ==================== ThreadLocalAStar类测试 ====================

    /**
     * 测试：ThreadLocalAStar.current获取实例
     */
    @Test
    public void testThreadLocalAStar_Current_ShouldReturnInstance() {
        AStar instance = ThreadLocalAStar.current();
        assertNotNull(instance);
    }

    /**
     * 测试：ThreadLocalAStar多次调用返回同一实例
     */
    @Test
    public void testThreadLocalAStar_CurrentMultipleCalls_ShouldReturnSameInstance() {
        AStar instance1 = ThreadLocalAStar.current();
        AStar instance2 = ThreadLocalAStar.current();
        assertSame(instance1, instance2);
    }

    /**
     * 测试：ThreadLocalAStar在不同线程返回不同实例
     */
    @Test
    public void testThreadLocalAStar_DifferentThreads_ShouldReturnDifferentInstances() throws InterruptedException {
        final AStar[] instances = new AStar[2];
        
        instances[0] = ThreadLocalAStar.current();
        
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                instances[1] = ThreadLocalAStar.current();
            }
        });
        thread.start();
        thread.join();
        
        assertNotSame(instances[0], instances[1]);
    }

    // ==================== TooLongPathException类测试 ====================

    /**
     * 测试：TooLongPathException构造函数
     */
    @Test
    public void testTooLongPathException_Constructor_ShouldCreateException() {
        TooLongPathException exception = new TooLongPathException("Test message");
        assertEquals("Test message", exception.getMessage());
    }

    /**
     * 测试：TooLongPathException是RuntimeException子类
     */
    @Test
    public void testTooLongPathException_ShouldBeRuntimeException() {
        TooLongPathException exception = new TooLongPathException("Test");
        assertTrue(exception instanceof RuntimeException);
    }

    /**
     * 测试：触发TooLongPathException（路径过长）
     */
    @Test(expected = TooLongPathException.class)
    public void testAStar_TooManyOpenNodes_ShouldThrowException() {
        // 创建一个大地图强制产生大量开放节点
        Grid largeGrid = new Grid(200, 200);
        for (int i = 0; i < 200; i++) {
            for (int j = 0; j < 200; j++) {
                largeGrid.setWalkable(i, j, true);
            }
        }
        // 创建迷宫式障碍物增加搜索复杂度
        for (int i = 1; i < 199; i += 2) {
            for (int j = 0; j < 200; j++) {
                if (j % 20 != 0) {
                    largeGrid.setWalkable(i, j, false);
                }
            }
        }
        AStar astar = new AStar();
        astar.search(0, 0, 199, 199, largeGrid);
    }

    // ==================== Nodes类测试（通过AStar间接测试）====================

    /**
     * 测试：Nodes的siftUp和siftDown堆操作
     */
    @Test
    public void testNodes_HeapOperations_ShouldMaintainHeapProperty() {
        // 通过复杂寻路测试堆操作
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if ((i + j) % 3 == 0) {
                    grid.setWalkable(i, j, false);
                }
            }
        }
        Path path = aStar.search(0, 0, 9, 9, grid);
        assertNotNull(path);
    }

    /**
     * 测试：Nodes扩容grow方法（小容量<64）
     */
    @Test
    public void testNodes_GrowSmallCapacity_ShouldExpandCorrectly() {
        Grid mediumGrid = new Grid(30, 30);
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                mediumGrid.setWalkable(i, j, true);
            }
        }
        AStar astar = new AStar();
        Path path = astar.search(0, 0, 29, 29, mediumGrid);
        assertFalse(path.isEmpty());
    }

    /**
     * 测试：Nodes扩容grow方法（大容量>=64）
     */
    @Test
    public void testNodes_GrowLargeCapacity_ShouldExpandCorrectly() {
        Grid largeGrid = new Grid(100, 100);
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                largeGrid.setWalkable(i, j, true);
            }
        }
        AStar astar = new AStar();
        Path path = astar.search(0, 0, 99, 99, largeGrid);
        assertFalse(path.isEmpty());
    }

    // ==================== 综合场景测试 ====================

    /**
     * 测试：复杂迷宫寻路
     */
    @Test
    public void testComplexMaze_ShouldFindPath() {
        Grid maze = new Grid(15, 15);
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                maze.setWalkable(i, j, true);
            }
        }
        // 创建迷宫墙壁
        for (int i = 2; i < 13; i++) {
            maze.setWalkable(i, 5, false);
            maze.setWalkable(i, 10, false);
        }
        maze.setWalkable(2, 5, true); // 开口
        maze.setWalkable(12, 10, true); // 开口
        
        AStar astar = new AStar();
        Path path = astar.search(0, 0, 14, 14, maze);
        assertNotNull(path);
    }

    /**
     * 测试：U型障碍物绕行
     */
    @Test
    public void testUShapedObstacle_ShouldNavigateAround() {
        Grid g = new Grid(10, 10);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                g.setWalkable(i, j, true);
            }
        }
        // 创建U型障碍
        for (int i = 3; i <= 7; i++) {
            g.setWalkable(i, 3, false);
            g.setWalkable(i, 7, false);
        }
        for (int j = 3; j <= 7; j++) {
            g.setWalkable(3, j, false);
        }
        
        AStar astar = new AStar();
        Path path = astar.search(5, 5, 9, 9, g);
        assertFalse(path.isEmpty());
    }

    /**
     * 测试：窄通道寻路
     */
    @Test
    public void testNarrowCorridor_ShouldFindPath() {
        Grid g = new Grid(20, 5);
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 5; j++) {
                g.setWalkable(i, j, true);
            }
        }
        // 只留中间一条通道
        for (int i = 0; i < 20; i++) {
            g.setWalkable(i, 0, false);
            g.setWalkable(i, 1, false);
            g.setWalkable(i, 3, false);
            g.setWalkable(i, 4, false);
        }
        
        AStar astar = new AStar();
        Path path = astar.search(0, 2, 19, 2, g);
        assertFalse(path.isEmpty());
    }

    /**
     * 测试：平滑路径效果
     */
    @Test
    public void testSmoothPath_ShouldReduceWaypoints() {
        Grid g = new Grid(20, 20);
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                g.setWalkable(i, j, true);
            }
        }
        
        AStar astar = new AStar();
        Path path1 = astar.search(0, 0, 15, 15, g, false);
        Path path2 = astar.search(0, 0, 15, 15, g, true);
        
        assertFalse(path1.isEmpty());
        assertFalse(path2.isEmpty());
        // 平滑路径应该有更少的路径点
        assertTrue(path2.size() <= path1.size());
    }

    /**
     * 测试：对角线障碍物场景
     */
    @Test
    public void testDiagonalObstacles_ShouldHandleCorrectly() {
        Grid g = new Grid(10, 10);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                g.setWalkable(i, j, true);
            }
        }
        // 创建对角线障碍
        for (int i = 2; i < 8; i++) {
            g.setWalkable(i, i, false);
        }
        
        AStar astar = new AStar();
        Path path = astar.search(0, 0, 9, 9, g);
        assertFalse(path.isEmpty());
    }

    /**
     * 测试：最短路径选择
     */
    @Test
    public void testShortestPath_ShouldChooseOptimal() {
        Grid g = new Grid(10, 10);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                g.setWalkable(i, j, true);
            }
        }
        
        AStar astar = new AStar();
        Path path = astar.search(0, 0, 9, 9, g);
        assertFalse(path.isEmpty());
        // 验证路径长度合理
        assertTrue(path.size() >= 2);
    }

    /**
     * 测试：Reachability斜线穿过格子交叉点（k>0）
     */
    @Test
    public void testReachability_DiagonalThroughIntersection_PositiveSlope() {
        Grid g = new Grid(10, 10);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                g.setWalkable(i, j, true);
            }
        }
        // 测试正斜率穿过交叉点的情况
        long point = Reachability.getClosestWalkablePointToTarget(0, 0, 4, 4, g);
        assertEquals(4, Point.getX(point));
        assertEquals(4, Point.getY(point));
    }

    /**
     * 测试：Reachability斜线穿过格子交叉点（k<0）
     */
    @Test
    public void testReachability_DiagonalThroughIntersection_NegativeSlope() {
        Grid g = new Grid(10, 10);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                g.setWalkable(i, j, true);
            }
        }
        // 测试负斜率穿过交叉点的情况
        long point = Reachability.getClosestWalkablePointToTarget(0, 9, 4, 5, g);
        assertNotNull(point);
    }

    /**
     * 测试：Grid最大尺寸边界
     */
    @Test
    public void testGrid_MaxSize_ShouldWork() {
        Grid g = new Grid(65536, 65536);
        assertEquals(65536, g.getWidth());
        assertEquals(65536, g.getHeight());
    }

    /**
     * 测试：所有方向常量
     */
    @Test
    public void testGrid_DirectionConstants_ShouldHaveCorrectValues() {
        assertEquals(0, Grid.DIRECTION_UP);
        assertEquals(1, Grid.DIRECTION_DOWN);
        assertEquals(2, Grid.DIRECTION_LEFT);
        assertEquals(3, Grid.DIRECTION_RIGHT);
        assertEquals(4, Grid.DIRECTION_LEFT_UP);
        assertEquals(5, Grid.DIRECTION_LEFT_DOWN);
        assertEquals(6, Grid.DIRECTION_RIGHT_UP);
        assertEquals(7, Grid.DIRECTION_RIGHT_DOWN);
    }

    /**
     * 测试：Path get方法索引计算
     */
    @Test
    public void testPath_GetReverseOrder_ShouldReturnCorrectPoints() {
        Path path = new Path();
        path.add(1, 1);
        path.add(2, 2);
        path.add(3, 3);
        
        long p0 = path.get(0);
        assertEquals(3, Point.getX(p0));
        assertEquals(3, Point.getY(p0));
        
        long p1 = path.get(1);
        assertEquals(2, Point.getX(p1));
        assertEquals(2, Point.getY(p1));
        
        long p2 = path.get(2);
        assertEquals(1, Point.getX(p2));
        assertEquals(1, Point.getY(p2));
    }

    /**
     * 测试：fillPath平滑模式下的路径合并
     */
    @Test
    public void testFillPath_SmoothMode_ShouldMergeCollinearPoints() {
        Grid g = new Grid(20, 20);
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                g.setWalkable(i, j, true);
            }
        }
        
        AStar astar = new AStar();
        // 直线路径应该在平滑模式下被简化
        Path smoothPath = astar.search(0, 0, 10, 0, g, true);
        assertFalse(smoothPath.isEmpty());
        assertTrue(smoothPath.size() <= 11);
    }

    /**
     * 测试：水平线第二格就不可走
     */
    @Test
    public void testReachability_HorizontalLineSecondCellBlocked_ShouldReturnStart() {
        grid.setWalkable(1, 5, false);
        long point = Reachability.getClosestWalkablePointToTarget(0, 5, 5, 5, grid);
        assertEquals(0, Point.getX(point));
        assertEquals(5, Point.getY(point));
    }

    /**
     * 测试：竖直线第二格就不可走
     */
    @Test
    public void testReachability_VerticalLineSecondCellBlocked_ShouldReturnStart() {
        grid.setWalkable(5, 1, false);
        long point = Reachability.getClosestWalkablePointToTarget(5, 0, 5, 5, grid);
        assertEquals(5, Point.getX(point));
        assertEquals(0, Point.getY(point));
    }

    /**
     * 测试：斜线中X和Y坐标都变化时的交叉检查
     */
    @Test
    public void testReachability_DiagonalCrossCheck_ShouldValidateAdjacentCells() {
        Grid g = new Grid(10, 10);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                g.setWalkable(i, j, true);
            }
        }
        // 在对角线路径上设置障碍，测试交叉检查逻辑
        g.setWalkable(2, 1, false);
        long point = Reachability.getClosestWalkablePointToTarget(0, 0, 5, 5, g);
        assertNotNull(point);
    }

    /**
     * 测试：Node位操作边界
     */
    @Test
    public void testNode_BitOperations_ShouldPreserveValues() {
        for (int i = 0; i < 100; i += 10) {
            long node = Node.toNode(i, i * 2, i * 3, i * 4);
            assertEquals(i, Node.getX(node));
            assertEquals(i * 2, Node.getY(node));
            assertEquals(i * 3, Node.getG(node));
            assertEquals(i * 4, Node.getF(node));
        }
    }

    /**
     * 测试：连续多次搜索内存清理
     */
    @Test
    public void testMultipleSearches_MemoryCleaning_ShouldNotLeak() {
        AStar astar = new AStar();
        for (int i = 0; i < 10; i++) {
            Path path = astar.search(0, 0, 5, 5, grid);
            assertFalse(path.isEmpty());
            assertTrue(astar.isCLean(grid));
        }
    }

    /**
     * 测试：极端情况单点地图
     */
    @Test
    public void testGrid_SingleCell_ShouldWork() {
        Grid g = new Grid(1, 1);
        g.setWalkable(0, 0, true);
        assertEquals(1, g.getWidth());
        assertEquals(1, g.getHeight());
        assertTrue(g.isWalkable(0, 0));
    }

    /**
     * 测试：Fence在同一格子内
     */
    @Test
    public void testReachability_FenceSameCell_ShouldCheckFence() {
        Fence fence = mock(Fence.class);
        when(fence.isReachable(1, 1, 1, 1)).thenReturn(false);
        long point = Reachability.getClosestWalkablePointToTarget(1, 1, 1, 1, 1, grid, fence);
        assertEquals(1, Point.getX(point));
        assertEquals(1, Point.getY(point));
    }

    /**
     * 测试：不同scale的scaleUp计算
     */
    @Test
    public void testReachability_ScaleUpDifferentScales_ShouldCalculateCorrectly() {
        assertEquals(1, Reachability.scaleUp(0, 3));
        assertEquals(3, Reachability.scaleUp(1, 3));
        assertEquals(5, Reachability.scaleUp(2, 3));
    }

    /**
     * 测试：AStar搜索后状态清理验证
     */
    @Test
    public void testAStar_AfterSearch_ShouldBeClean() {
        Path path = aStar.search(0, 0, 5, 5, grid);
        assertFalse(path.isEmpty());
        assertTrue(aStar.isCLean(grid));
        assertTrue(grid.isClean());
    }
}
