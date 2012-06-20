import entity

class LaserGun(entity.Entity):
    """Laser-carrying entity"""
    quantity = 0 
    name = "LaserGun"

    def __init__():
        set_loc(0, 0)
        self.inst = self.name + "#" + str(quantity)
        quantity += 1

    def __init__(x, y):
        set_loc(x, y)
        self.inst = self.name + "#" + str(quantity)
        quantity += 1


