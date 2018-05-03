require 'pry'

class Triangle
  attr_accessor :x, :y, :z

  def initialize(a = nil, b = nil, c = nil)
    while !a || !b || !c
      random_a = Random.rand(1..20) unless a 
      random_b = Random.rand(1..20) unless b
      random_c = Random.rand(1..20) unless c

      if not_trinagle(a, b, c, random_a, random_b, random_c)
        next
      else
        a = random_a unless a
        b = random_b unless b
        c = random_c unless c
        break
      end
    end

    if not_trinagle(a, b, c)
      raise "\n\nNot a triangle\n\n" 
    end
    @x = a
    @y = b
    @z = c
  end

  def get_perimeter
    @x + @y + @z
  end


  def get_area
    sp = get_perimeter / 2
    Math.sqrt(sp * (sp - @x) * (sp - @y) * (sp - @z))
  end

  def uniform_enlargement(percent)
    @x += @x * percent / 100.0
    @y += @y * percent / 100.0
    @z += @z * percent / 100.0
  end

  def output_edges
    puts "a = #{@x}", "b = #{@y}", "c = #{@z}"
  end

  def not_trinagle(a = nil, b = nil, c = nil, random_a = nil, random_b = nil, random_c = nil)
    a = random_a unless a
    b = random_b unless b
    c = random_c unless c

    (a + b) < c || (a + c) < b || (b + c) < a
  end
end