import math

class RectEntity:
    """Basic rectangle intended for optimisation in collision detection"""
    def __init__(self, x, y, r, b):
        self.x = x
        self.y = y
        self.r = r
        self.b = b 

    def check_rect(self, ge):
        if (self.x > ge.r || ge.x > self.r || \
            self.y > ge.b || ge.y > self.b):
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
        else
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

