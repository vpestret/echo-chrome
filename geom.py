import math

class RectEntity:
    """Basic rectangle intended for optimisation in collision detection"""
    def __init__(self, re_x, re_y, re_r, re_b):
        self.re_x = re_x
        self.re_y = re_y
        self.re_r = re_r
        self.re_b = re_b 

    def check_rect(self, ge):
        if (self.re_x > ge.re_r or ge.re_x > self.re_r or \
            self.re_y > ge.re_b or ge.re_y > self.re_b):
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
        self.re_x = min([self.cx, self.cx+self.vx])
        self.re_r = max([self.cx, self.cx+self.vx])
        self.re_y = min([self.cy, self.cy+self.vy])
        self.re_b = max([self.cy, self.cy+self.vy])

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
            v2 = self.v*self.v
            # just squared v+r > s
            if not(v2+2*self.v*it.r+r2 > s2):
                return (False, 0.0)
            # scalar multiplication
            smv = (it.cx-self.cx)*self.vx + (it.cy-self.cy)*self.vy
            if not(smv > 0):
                return (False, 0.0)
            # vector multiplication
            sxv = (it.cx-self.cx)*self.vy - (it.cy-self.cy)*self.vx
            d2 = sxv*sxv/v2
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
        self.re_x = self.cx - self.r
        self.re_r = self.cx + self.r
        self.re_y = self.cy - self.r
        self.re_b = self.cy + self.r

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
        self.re_x = self.cx - self.w/2
        self.re_r = self.cx + self.w/2
        self.re_y = self.cy - self.h/2
        self.re_b = self.cy + self.h/2

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


def tk_draw_vector(canvas, vec, margin, lwidth = 1, lcolor = 'black',\
                   draw_origin = True):
    # draw origin
    if (draw_origin):
        canvas.create_line(margin + vec.cx - 5, margin + vec.cy - 5,\
                           margin + vec.cx + 5, margin + vec.cy + 5,\
                           width=lwidth, fill=lcolor)
        canvas.create_line(margin + vec.cx + 5, margin + vec.cy - 5,\
                           margin + vec.cx - 5, margin + vec.cy + 5,\
                           width=lwidth, fill=lcolor)
    # draw vector itself
    canvas.create_line(margin + vec.cx, margin + vec.cy,\
                       margin + vec.cx + vec.vx, margin + vec.cy + vec.vy,\
                       width=lwidth, fill=lcolor)

def tk_draw_circle(canvas, circ, margin):
    canvas.create_oval(margin + circ.cx - circ.r, margin + circ.cy - circ.r,\
                       margin + circ.cx + circ.r, margin + circ.cy + circ.r)

def gen_vectors(num_vectors, max_x, max_y, max_v):
    vectors = []
    #vectors.append(Vector2(286,\
    #                   194,\
    #                   -30,\
    #                   46))
    for idx in range(num_vectors):
        vectors.append(Vector2(random.randint(0,max_x), \
                               random.randint(0,max_y), \
                               random.randint(-max_v,max_v), \
                               random.randint(-max_v,max_v)))
    return vectors

def gen_circles(num_circles, max_x, max_y, min_r, max_r):
    circles = []
    #circles.append(Circle(329,\
    #                   151,\
    #                   43))
    for idx in range(num_circles):
        circles.append(Circle(random.randint(0,max_x), \
                               random.randint(0,max_y), \
                               random.randint(min_r,max_r)))
    return circles

class Painter:
    def __init__(self, canvas, nv, nc, max_x, max_y, max_v,\
                 min_r, max_r, margin):
        self.canvas = canvas
        self.num_vectors = nv
        self.num_circles = nc
        self.max_x = max_x
        self.max_y = max_y
        self.max_v = max_v
        self.min_r = min_r
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
                           self.margin, 1)
    
        # prepare circles
        circles = gen_circles(self.num_circles, self.max_x,\
                              self.max_y, self.min_r, self.max_r)
        # draw
        for circ in circles:
            tk_draw_circle(self.canvas, circ,\
                           self.margin)

        # collide vectors with circles
        print ('--------------- Next scene -------------------')
        for vec in vectors:
            for circ in circles:
                (fact, ratio) = vec.collide(circ)
                if (fact):
                    tk_draw_vector(self.canvas,
                                   Vector2(vec.cx + vec.vx*(1-ratio),\
                                           vec.cy + vec.vy*(1-ratio),\
                                           vec.vx*ratio, vec.vy*ratio), 
                                   self.margin, 2, 'red', False)
                    print ('Vector2(%d, \n\
                       %d, \n\
                       %d, \n\
                       %d))' % (vec.cx, vec.cy, vec.vx, vec.vy))

                    print ('vs Circle(%d, \n\
                          %d, \n\
                          %d))' % (circ.cx, circ.cy, circ.r))

def draw_painter(event):
    global painter
    painter.draw(event)

def print_event(event):
    print 'Rbut(%d,%d)' % (event.x-55, event.y-55)

def test():
    num_vectors = 10
    num_circles = 10
    max_x = 400
    max_y = 200
    max_v = 50
    min_r = 10
    max_r = 50
    margin = max([max_v, max_r]) + 5

    # draw everything
    root = Tkinter.Tk()
    root.title('geom module test')
    root.geometry(repr(max_x+2*margin) + 'x' + repr(max_y+2*margin))
    canvas = Tkinter.Canvas(root, width=max_x+2*margin, height=max_y+2*margin)
    canvas.grid(column=0, row=0,\
                sticky=(Tkinter.N, Tkinter.W, Tkinter.E, Tkinter.S))

    global painter
    painter = Painter(canvas, num_vectors, num_circles,\
                      max_x, max_y, max_v, min_r, max_r,\
                      margin)

    canvas.bind('<Button-1>', draw_painter)
    canvas.bind('<Button-3>', print_event)

    painter.draw(None)

    root.mainloop()
    

if __name__ == '__main__':
    test()

