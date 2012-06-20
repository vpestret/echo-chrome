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
        self.__init__(self, cx, cy, 0, 0)

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
        if not(isinstance(it, Circle)):
            raise Exception('collides other than vector with circle are not'\
                            + ' supported')
        # optimisation
        if not(self.check_rect(it)):
            return (False, 0)
        # normal flow
        raise Exception('not yet implemented')

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
        if not(isinstance(it, Vector2)):
            raise Exception('collides other than vector with circle are not'\
                            + ' supported')
        return it.collide(self)

