[define,nat<=int[is>0]]
[define,person<=('name'->str,'age'->nat)]
[define,vertex<=person:('name'->str,'age'->nat)-<(
                        'id'    -> .age,
                        'label' -> <x>.name[plus,<.x>.age[as,str]],
                        'outE'  -> {0})]

