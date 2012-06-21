import math

class RectEntity:
    """Basic rectangle intended for optimisation in collision detection"""
    def __init__(self, x, y, r, b):
        self.x = x
        self.y = y
        self.r = r
        self.b = b 

    def check_rect(self, ge):
        if (self.x > ge.r or ge.x > self.r or \
            self.y > ge.b or ge.y > self.b):
            return False
        else:
            return True

class Vector2(RectEntity):
    """2D vector"""
    def __init__(self, cx, cy):
        self.__init__(self, cx, cy, 0.0, 0.0)

    def __init__(self, cx, cy, vx, vy):
        self.cx = cx
        self.cy = cy
        self.vx = vx
        self.vy = vy
        self.v = math.sqrt(vx*vx + vy*vy)
        self.update_rect()
    
    def update_rect(self):
        self.x = min([self.cx, self.cx+self.vx])
        self.r = max([self.cx, self.cx+self.vx])
        self.y = min([self.cy, self.cy+self.vy])
        self.b = max([self.cy, self.cy+self.vy])

    def r2(self):
        return self.v*self.v

    def r(self):
        return self.v

    def collide(self, it):
        """returns tuple first boolean collide indicator,\
           second - the collide ratio if any"""
        if (isinstance(it, Circle)):
            # optimisation
            if not(self.check_rect(it)):
                return (False, 0.0)
            # normal flow
            s2 = (it.cx-self.cx)*(it.cx-self.cx)\
                  + (it.cy-self.cy)*(it.cy-self.cy)
            r2 = it.r*it.r
            if (s2 < r2):
                return (True, 1.0)
            # beginning is not inside the circle
            v4 = self.v*self.v*self.v*self.v
            s4 = s2*s2
            r4 = r2*r2
            # just double squared v > s-r
            if not(v4+s4+r4 > 2*v2*s2+2*s2*r2+2*v2*r2):
                return (False, 0.0)
            sxv = (it.cx-self.cx)*self.vy - (it.cy-self.cy)*self.vx
            d2 = sxv*sxv/(self.v*self.v)
            # just squared d < r
            if not(d2 < r2):
                return (False, 0.0) # fly by w/o impact
            dti = math.sqrt(s2-d2) - math.sqrt(r2-d2) # distance to impact
            # last check is not squared
            if not(self.v > dti):
                return (False, 0.0)
            return (True, (self.v - dti)/self.v)

        elif (isinstance(it, Box)):
            # optimisation
            if not(self.check_rect(it)):
                return (False, 0.0)
            raise Exception('not yet implemented')
        else:
            raise Exception('collides of Vector2 other than ones with Circle'\
                            + ' or with Box' \
                            + ' are not supported')

class Circle(RectEntity):
    """Circle"""
    def __init__(self, cx, cy, r):
        self.cx = cx
        self.cy = cy
        self.r = r
        self.update_rect()

    def update_rect(self):
        self.x = self.cx - self.r
        self.r = self.cx + self.r
        self.y = self.cy - self.r
        self.b = self.cy + self.r

    def collide(self, it):
        """returns tuple first boolean collide indicator,\
           second - the collide ratio if any"""
        if (isinstance(it, Vector2)):
            return it.collide(self)
        elif (isinstance(it, Circle)):
            # optimisation
            if not(self.check_rect(it)):
                return (False, 0.0)
            # normal flow
            s2 = (it.cx-self.cx)*(it.cx-self.cx)\
                  + (it.cy-self.cy)*(it.cy-self.cy)
            if not(s2 < (self.r+it.r)*(self.r+it.r)):
                return (False, 0.0)
            return (True, (self.r+it.r - math.sqrt(s2))/(self.r+it.r))

        elif (isinstance(it, Box)):
            # optimisation
            if not(self.check_rect(it)):
                return (False, 0.0)
            raise Exception('not yet implemented')
        else:
            raise Exception('collides of Circle other than ones with Vector2'\
                            + ' or with Circle' \
                            + ' or with Box' \
                            + ' are not supported')

class Box(RectEntity):
    """Box"""
    def __init__(self, cx, cy, w, h):
        self.cx = cx
        self.cy = cy
        self.w = w
        self.h = h
        self.update_rect()

    def update_rect(self):
        self.x = self.cx - self.w/2
        self.r = self.cx + self.w/2
        self.y = self.cy - self.h/2
        self.b = self.cy + self.h/2

    def collide(self, it):
        """returns tuple first boolean collide indicator,\
           second - the collide ratio if any"""
        if (isinstance(it, Vector2)):
            return it.collide(self)
        elif (isinstance(it, Circle)):
            return it.collide(self)
        elif (isinstance(it, Box)):
            # Box is the same as Rect
            return (self.check_rect(it), 0.0) # TODO: collide ratio 
        else:
            raise Exception('collides of Box other than ones with Vector2'\
                            + ' or with Circle' \
                            + ' or with Box' \
                            + ' are not supported')

import random
import Tkinter


def tk_draw_vector(canvas, vec, margin):
    # draw origin
    canvas.create_line((margin + vec.cx - 5, margin + vec.cy - 5,\
                        margin + vec.cx + 5, margin + vec.cy + 5))
    canvas.create_line((margin + vec.cx + 5, margin + vec.cy - 5,\
                        margin + vec.cx - 5, margin + vec.cy + 5))
    # draw vector itself
    canvas.create_line((margin + vec.cx, margin + vec.cy,\
                        margin + vec.cx + vec.vx, margin + vec.cy + vec.vy))

def tk_draw_circle(canvas, circ):
    pass

def gen_vectors(num_vectors, max_x, max_y, max_v):
    vectors = []
    for idx in range(num_vectors):
        vectors.append(Vector2(random.randint(0,max_x), \
                               random.randint(0,max_y), \
                               random.randint(-max_v,max_v), \
                               random.randint(-max_v,max_v)))
    return vectors

class Painter:
    def __init__(self, canvas, nv, nc, max_x, max_y, max_v, max_r, margin):
        self.canvas = canvas
        self.num_vectors = nv
        self.num_circles = nc
        self.max_x = max_x
        self.max_y = max_y
        self.max_v = max_v
        self.max_r = max_r
        self.margin = margin

    def draw(self, event):
        self.canvas.delete(Tkinter.ALL)
        # prepare vectors
        vectors = gen_vectors(self.num_vectors, self.max_x,\
                              self.max_y, self.max_v)
        # draw
        for vec in vectors:
            tk_draw_vector(self.canvas, vec,\
                           self.margin)

def draw_painter(event):
    global painter
    painter.draw(event)


def test():
    num_vectors = 10
    num_circles = 10
    max_x = 400
    max_y = 200
    max_v = 50
    max_r = 50
    margin = max([max_v, max_r]) + 5

    # prepare circles
    circles = []
    for idx in range(num_circles):
        circles.append(Circle(random.randint(0,max_x), \
                               random.randint(0,max_y), \
                               random.randint(0,max_v)))

    # draw everything
    root = Tkinter.Tk()
    root.title('geom module test')
    root.geometry(repr(max_x+2*margin) + 'x' + repr(max_y+2*margin))
    canvas = Tkinter.Canvas(root)
    canvas.grid(column=0, row=0,\
                sticky=(Tkinter.N, Tkinter.W, Tkinter.E, Tkinter.S))

    global painter
    painter = Painter(canvas, num_vectors, num_circles,\
                      max_x, max_y, max_r, max_v,\
                      margin)

    canvas.bind("<Button-1>", draw_painter)

    root.mainloop()
    

if __name__ == '__main__':
    test()

