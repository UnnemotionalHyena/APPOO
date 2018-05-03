class Rectangle
  attr_accessor :x, :y

  def initialize(length = Random.rand(1..20), height = Random.rand(1..20))
    @x = length
    @y = height
  end

  def get_perimeter
    @x * 2 + @y * 2
  end


  def get_area
    @x * @y
  end

  def uniform_enlargement(percent)
    @x += @x * percent / 100.0
    @y += @y * percent / 100.0
  end

  def output_edges
    puts "a = #{@x}", "b = #{@y}", "c = #{@x}", "z = #{@y}"
  end
end

