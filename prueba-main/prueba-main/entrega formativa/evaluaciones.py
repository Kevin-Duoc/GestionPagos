import funcion as fn 
paquete=[]
while True:
    print("bienvenidos al sistema de paqueteria")
    print("opcion 1.Registrar pedido")
    print("opcion 2.Listar todos los pedidos")
    print("opcion 3.Inmprimir hoja de ruta")
    print("opcion 4.Salir del programa")
    opcion = int(input("ingrese la opcion que desee ejecutar:\n"))
    if opcion == 1:
        fn.registro_pedido(paquete)
    elif opcion == 2:
       fn.listar_pedidos()
    elif opcion == 3:
        fn.donde
    elif opcion == 4:
        print("saliendo del programa")
        break
    else:
        print("opcion no valida, vuelva a ingresar una opcion")