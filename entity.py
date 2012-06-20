import geom

class Entity:
    """class which describes entities"""

    name = "__DEFAULT__ENTITY__"
    inst = "__DEFAULT_INSTANCE__"
    def __init__():
        self.loc = geom.Vector2(0, 0)

    def __init__(x, y):
        self.loc = geom.Vector2(x, y)

    def get_loc(self):
        return self.loc

    def set_loc(self, x, y):
        self.loc = geom.Vector2(x, y)

    def get_name():
        return self.name

    def get_inst():
        return self.inst

