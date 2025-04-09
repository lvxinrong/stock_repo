import re


def calculate_total(file_path):
    total = 0.0
    with open(file_path, "r", encoding='utf-8') as file:
        for line in file:
            print(line)
            # 提取冒号后的数字部分并转换为浮点数
            value = line.strip().split(": ")[1].lstrip()
            total += float(value)

    return total


if __name__ == '__main__':
    print(calculate_total("H:\\yield_rate.txt"))
