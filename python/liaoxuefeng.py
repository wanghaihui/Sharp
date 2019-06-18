from method import my_abs

a = 100
if a >= 0:
    print(a)
else:
    print(-a)


print('''line1
line2
line3
''')

print(r'''hello,\n
world''')

s3 = r'Hello, "Bart"'
s4 = r'''Hello,
Lisa!'''
print s3
print s4

print('%2d-%02d' % (3, 1))
print('%.2f' % 3.1415926)

sum = 0
for x in range(101):
    sum = sum + x
print(sum)

L = ['Bart', 'Lisa', 'Adam']
for str in L:
    print("Hello, %s" % str)

print(hex(255))
print(hex(1000))

print(my_abs(-2))
