from openai import OpenAI


def get_deepseek_client() -> OpenAI:
    return OpenAI(api_key=get_token(), base_url="https://api.deepseek.com")


def get_token():
    # 打开文件并读取整个内容
    with open('H:\\stock\\deepseek_token.txt', 'r') as file:
        content = file.read()
        return content


if __name__ == '__main__':
    print(get_deepseek_client())