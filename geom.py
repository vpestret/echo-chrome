import math

class RectEntity:
    """Basic rectangle intended for optimisation in collision detection"""
    def __init__(self, re_x, re_y, re_r, re_b):
        self.re_x = re_x
        self.re_y = re_y
        self.re_r = re_r
        self.re_b = re_b 

    def check_rect(self, it):
        if (self.re_x > it.re_r or it.re_x > self.re_r or \
            self.re_y > it.re_b or it.re_y > self.re_b):
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
            in_n_corner = False
            in_ne_corner = False
            in_e_corner = False
            in_se_corner = False
            in_s_corner = False
            in_sw_corner = False
            in_w_corner = False
            in_nw_corner = False
            if (self.cx > it.re_r):
                if (self.cy < it.re_y):
                    in_ne_corner = True
                elif (self.cy > it.re_b):
                    in_se_corner = True
                else:
                    in_e_corner = True
            elif (self.cx < it.re_x):
                if (self.cy < it.re_y):
                    in_nw_corner = True
                elif (self.cy > it.re_b):
                    in_sw_corner = True
                else:
                    in_w_corner = True
            else:
                if (self.cy < it.re_y):
                    in_n_corner = True
                elif (self.cy > it.re_b):
                    in_s_corner = True
                else:
                    return (True, 1.0)
            if (in_n_corner):
                if (self.vx == 0.0):
                    return (True, float(self.vy - (it.re_y - self.cy))/self.vy)
                ix = float((it.re_y - self.cy)*self.vx)/self.vy + self.cx
                if (it.re_x < ix and ix < it.re_r):
                    return (True, float(self.vy - (it.re_y - self.cy))/self.vy)
                return (False, 0.0)
            elif (in_s_corner):
                if (self.vx == 0.0):
                    return (True, float(self.vy - (it.re_b - self.cy))/self.vy)
                ix = float((it.re_b - self.cy)*self.vx)/self.vy + self.cx
                if (it.re_x < ix and ix < it.re_r):
                    return (True, float(self.vy - (it.re_b - self.cy))/self.vy)
                return (False, 0.0)
            elif (in_w_corner):
                if (self.vy == 0.0):
                    return (True, float(self.vx - (it.re_x - self.cx))/self.vx)
                iy = float((it.re_x - self.cx)*self.vy)/self.vx + self.cy
                if (it.re_y < iy and iy < it.re_b):
                    return (True, float(self.vx - (it.re_x - self.cx))/self.vx)
                return (False, 0.0)
            elif (in_e_corner):
                if (self.vy == 0.0):
                    return (True, float(self.vx - (it.re_r - self.cx))/self.vx)
                iy = float((it.re_r - self.cx)*self.vy)/self.vx + self.cy
                if (it.re_y < iy and iy < it.re_b):
                    return (True, float(self.vx - (it.re_r - self.cx))/self.vx)
                return (False, 0.0)
            elif (in_ne_corner):
                ix = float((it.re_y - self.cy)*self.vx)/self.vy + self.cx
                if (it.re_x < ix and ix < it.re_r):
                    return (True, float(self.vy - (it.re_y - self.cy))/self.vy)
                iy = float((it.re_r - self.cx)*self.vy)/self.vx + self.cy
                if (it.re_y < iy and iy < it.re_b):
                    return (True, float(self.vx - (it.re_r - self.cx))/self.vx)
                return (False, 0.0)
            elif (in_se_corner):
                ix = float((it.re_b - self.cy)*self.vx)/self.vy + self.cx
                if (it.re_x < ix and ix < it.re_r):
                    return (True, float(self.vy - (it.re_b - self.cy))/self.vy)
                iy = float((it.re_r - self.cx)*self.vy)/self.vx + self.cy
                if (it.re_y < iy and iy < it.re_b):
                    return (True, float(self.vx - (it.re_r - self.cx))/self.vx)
                return (False, 0.0)
            elif (in_sw_corner):
                ix = float((it.re_b - self.cy)*self.vx)/self.vy + self.cx
                if (it.re_x < ix and ix < it.re_r):
                    return (True, float(self.vy - (it.re_b - self.cy))/self.vy)
                iy = float((it.re_x - self.cx)*self.vy)/self.vx + self.cy
                if (it.re_y < iy and iy < it.re_b):
                    return (True, float(self.vx - (it.re_x - self.cx))/self.vx)
                return (False, 0.0)
            elif (in_nw_corner):
                ix = float((it.re_y - self.cy)*self.vx)/self.vy + self.cx
                if (it.re_x < ix and ix < it.re_r):
                    return (True, float(self.vy - (it.re_y - self.cy))/self.vy)
                iy = float((it.re_x - self.cx)*self.vy)/self.vx + self.cy
                if (it.re_y < iy and iy < it.re_b):
                    return (True, float(self.vx - (it.re_x - self.cx))/self.vx)
                return (False, 0.0)
            raise Exception('control flow reached unexpectedly this point')
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
           second - the 1.0"""
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
            return (True, 1.0)

        elif (isinstance(it, Box)):
            # optimisation
            if not(self.check_rect(it)):
                return (False, 0.0)
            in_ne_corner = False
            in_se_corner = False
            in_sw_corner = False
            in_nw_corner = False
            if (self.cx > it.re_r):
                if (self.cy < it.re_y):
                    in_ne_corner = True
                elif (self.cy > it.re_b):
                    in_se_corner = True
            elif (self.cx < it.re_x):
                if (self.cy < it.re_y):
                    in_nw_corner = True
                elif (self.cy > it.re_b):
                    in_sw_corner = True
            if (in_nw_corner):
                s2_1 = (it.re_x-self.cx)*(it.re_x-self.cx)\
                      + (it.re_y-self.cy)*(it.re_y-self.cy)
                if not(s2_1 < self.r*self.r):
                    return (False, 0.0)
            elif (in_ne_corner):
                s2_2 = (it.re_r-self.cx)*(it.re_r-self.cx)\
                      + (it.re_y-self.cy)*(it.re_y-self.cy)
                if not(s2_2 < self.r*self.r):
                    return (False, 0.0)
            elif (in_se_corner):
                s2_3 = (it.re_r-self.cx)*(it.re_r-self.cx)\
                      + (it.re_b-self.cy)*(it.re_b-self.cy)
                if not(s2_3 < self.r*self.r):
                    return (False, 0.0)
            elif (in_sw_corner):
                s2_4 = (it.re_x-self.cx)*(it.re_x-self.cx)\
                      + (it.re_b-self.cy)*(it.re_b-self.cy)
                if not(s2_4 < self.r*self.r):
                    return (False, 0.0)
            return (True, 1.0)
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
           second - the 1.0"""
        if (isinstance(it, Vector2)):
            return it.collide(self)
        elif (isinstance(it, Circle)):
            return it.collide(self)
        elif (isinstance(it, Box)):
            # Box is the same as Rect
            is_collided = self.check_rect(it)
            return (is_collided, 1.0*float(is_collided))
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

def tk_draw_arc(canvas, circ, margin, start, extent,\
                lwidth = 1, lcolor = 'black'):
    canvas.create_arc(margin + circ.cx - circ.r, margin + circ.cy - circ.r,\
                      margin + circ.cx + circ.r, margin + circ.cy + circ.r,\
                      start = start, extent = extent,\
                      width=lwidth, style = Tkinter.ARC, outline=lcolor)

def tk_draw_box(canvas, box, margin, lwidth = 1, lcolor = 'black'):
    # draw top, right, bottom, left
    canvas.create_line(margin + box.re_x, margin + box.re_y,\
                       margin + box.re_r, margin + box.re_y,\
                       width=lwidth, fill=lcolor)
    canvas.create_line(margin + box.re_r, margin + box.re_y,\
                       margin + box.re_r, margin + box.re_b,\
                       width=lwidth, fill=lcolor)
    canvas.create_line(margin + box.re_r, margin + box.re_b,\
                       margin + box.re_x, margin + box.re_b,\
                       width=lwidth, fill=lcolor)
    canvas.create_line(margin + box.re_x, margin + box.re_b,\
                       margin + box.re_x, margin + box.re_y,\
                       width=lwidth, fill=lcolor)

def gen_vectors(num_vectors, max_x, max_y, max_v):
    vectors = []
    for idx in range(num_vectors):
        vectors.append(Vector2(random.randint(0,max_x), \
                               random.randint(0,max_y), \
                               random.randint(-max_v,max_v), \
                               random.randint(-max_v,max_v)))
    return vectors

def gen_circles(num_circles, max_x, max_y, min_r, max_r):
    circles = []
    for idx in range(num_circles):
        circles.append(Circle(random.randint(0,max_x), \
                               random.randint(0,max_y), \
                               random.randint(min_r,max_r)))
    return circles

def gen_boxes(num_boxes, max_x, max_y, min_h, max_h):
    boxes = []
    for idx in range(num_boxes):
        boxes.append(Box(random.randint(0,max_x), \
                               random.randint(0,max_y), \
                               random.randint(min_h,max_h), \
                               random.randint(min_h,max_h)))
    return boxes

class Painter:
    def __init__(self, canvas, nv, nc, nr, max_x, max_y, max_v,\
                 min_r, max_r, min_h, max_h, margin):
        self.canvas = canvas
        self.num_vectors = nv
        self.num_circles = nc
        self.num_boxes = nr
        self.max_x = max_x
        self.max_y = max_y
        self.max_v = max_v
        self.min_r = min_r
        self.max_r = max_r
        self.min_h = min_h
        self.max_h = max_h
        self.margin = margin

    def draw(self, event):
        self.canvas.delete(Tkinter.ALL)
        self.canvas.create_text(self.margin+self.max_x/2,10,\
            text='LMouseBut next scene, RMouseBut print coords to console',\
            fill='purple')

        # prepare vectors
        self.vectors = gen_vectors(self.num_vectors, self.max_x,\
                              self.max_y, self.max_v)
        # draw
        for vec in self.vectors:
            tk_draw_vector(self.canvas, vec,\
                           self.margin, 1)
    
        # prepare circles
        self.circles = gen_circles(self.num_circles, self.max_x,\
                              self.max_y, self.min_r, self.max_r)
        # draw
        for circ in self.circles:
            tk_draw_circle(self.canvas, circ,\
                           self.margin)

        # prepare boxes
        self.boxes = gen_boxes(self.num_boxes, self.max_x,\
                              self.max_y, self.min_h, self.max_h)
        # draw
        for box in self.boxes:
            tk_draw_box(self.canvas, box,\
                           self.margin, 1)
        
        # put circles and boxes in the one array for unification
        figs = []
        figs.extend(self.circles)
        figs.extend(self.boxes)

        # collide vectors with figures (circles and boxes)
        print ('--------------- Next scene -------------------')
        for vec in self.vectors:
            for fig in figs:
                (fact, ratio) = vec.collide(fig)
                if (fact):
                    tk_draw_vector(self.canvas,
                                   Vector2(vec.cx + vec.vx*(1-ratio),\
                                           vec.cy + vec.vy*(1-ratio),\
                                           vec.vx*ratio, vec.vy*ratio), 
                                   self.margin, 2, 'red', ratio>0.999)
                    print ('collide Vector2(%d, \n\
                       %d, \n\
                       %d, \n\
                       %d)' % (vec.cx, vec.cy, vec.vx, vec.vy))

                    if isinstance(fig, Circle):
                        print ('with Circle(%d, \n\
                              %d, \n\
                              %d)\n>>>>>' % (fig.cx, fig.cy, fig.r))
                    else:
                        print ('with Box(%d, \n\
                              %d, \n\
                              %d, \n\
                              %d)\n>>>>>' % (fig.cx, fig.cy, fig.h, fig.w))

        # collide figures with figures
        for idx1 in range(len(figs)):
            for idx2 in range(idx1):
                fig1 = figs[idx1]
                fig2 = figs[idx2]
                (fact,ratio) = fig1.collide(fig2)
                if (fact):
                    # 1
                    if isinstance(fig1, Circle):
                        tk_draw_arc(self.canvas, fig1, self.margin,\
                            0, 359.9, 2, 'green')
                        print ('collide Circle(%d, \n\
                              %d, \n\
                              %d)' % (fig1.cx, fig1.cy, fig1.r))
                    else:
                        tk_draw_box(self.canvas, fig1, self.margin,\
                            2, 'blue')
                        print ('collide Box(%d, \n\
                              %d, \n\
                              %d, \n\
                              %d)' % (fig1.cx, fig1.cy, fig1.h, fig1.w))
                    # 2
                    if isinstance(fig2, Circle):
                        tk_draw_arc(self.canvas, fig2, self.margin,\
                            0, 359.9, 2, 'green')
                        print ('with Circle(%d, \n\
                              %d, \n\
                              %d)\n>>>>>' % (fig2.cx, fig2.cy, fig2.r))
                    else:
                        tk_draw_box(self.canvas, fig2, self.margin,\
                            2, 'blue')
                        print ('with Box(%d, \n\
                              %d, \n\
                              %d, \n\
                              %d)\n>>>>>' %\
                              (fig2.cx, fig2.cy, fig2.h, fig2.w))

def draw_painter(event):
    global painter
    painter.draw(event)

def print_event(event):
    global painter
    print 'Rbut(%d,%d)' % (event.x-painter.margin, event.y-painter.margin)

def test():
    num_vectors = 10
    num_circles = 5
    num_boxes = 5
    max_x = 400
    max_y = 200
    max_v = 50
    min_r = 10
    max_r = 50
    min_h = 10
    max_h = 50
    margin = max([max_v, max_r]) + 5

    # draw everything
    root = Tkinter.Tk()
    root.title('geom module test')
    root.geometry(repr(max_x+2*margin) + 'x' + repr(max_y+2*margin))
    canvas = Tkinter.Canvas(root, width=max_x+2*margin, height=max_y+2*margin)
    canvas.grid(column=0, row=0,\
                sticky=(Tkinter.N, Tkinter.W, Tkinter.E, Tkinter.S))

    global painter
    painter = Painter(canvas, num_vectors, num_circles, num_boxes,\
                      max_x, max_y, max_v, min_r, max_r,\
                      min_h, max_h, margin)

    canvas.bind('<Button-1>', draw_painter)
    canvas.bind('<Button-3>', print_event)

    painter.draw(None)

    root.mainloop()
    

if __name__ == '__main__':
    test()

