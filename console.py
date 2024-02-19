import requests

print("Blog Control Console")
print("Frontend version: 1.0")

comInt = 1
authKey = "a8a608f7489b2f60e76a96255a29929d"
apiURL = "https://me.hysong.dev/blog/api?action=ACTION&section=SECTION&auth=AUTH&file=FILE"
sectionsList = ["featured", "idea", "done", "progress", "self"]
data = requests.get(apiURL.replace("ACTION", "ver")).json()
print(f"Backend version: {data['version']}")

if data['comInt'] != comInt:
    print("WARNING: The frontend and backend versions do not match. This may cause issues.")
else:
    print("The frontend and backend versions match. This is good.")

print("Type 'help' for a list of commands")

def getListOfFiles(section: str):
    # Send request to API
    # Return list of files
    newURL = apiURL.replace("ACTION", "list").replace("SECTION", section).replace("AUTH", authKey)
    response = requests.get(newURL).json()['data']
    return response

def getFileContent(section: str, file: str):
    # Send request to API
    # Return file content
    newURL = apiURL.replace("ACTION", "get").replace("SECTION", section).replace("AUTH", authKey).replace("FILE", file)
    response = requests.get(newURL).json()
    return response

def deleteFile(section: str, file: str):
    # Send request to API
    # Return success or error
    newURL = apiURL.replace("ACTION", "delete").replace("SECTION", section).replace("AUTH", authKey).replace("FILE", file)
    response = requests.get(newURL).json()
    return response

def settingsState():
    print(f"Current settings: \n\tAuth Key: {authKey}\n\tAPI URL: {apiURL}")

def getSectionFromInput(sectionIn):
    try:
        section = sectionsList[int(sectionIn) - 1]
        return section
    except:
        return sectionIn

def getFileFromInput(fileIn, fileList):
    try:
        file = fileList[int(fileIn) - 1]['name']
        return file
    except:
        return fileIn

def help():
    print("Commands:\n")
    print("\tls  - List files in a section")
    print("\tlss - List sections")
    print("\tlsa - List all files")
    print("\tget - Get file content")
    print("\trm  - Delete file")
    print("\tset - Change settings")
    print("\thelp - Show this message")
    print("\tcopy - Download markdown file")
    print("\texit - Exit console")

while True:
    try:
        userInput = input("console >>> ")
        if userInput.startswith("ls "):
            section = getSectionFromInput(userInput.split(" ")[1])
            print(f"Files in {section}:")
            list = getListOfFiles(section)
            for i in range(len(list)):
                obj = list[i]
                print(f"\t{i+1}. {obj['title']} ({obj['name']})")

        elif userInput == "lss":
            print("Sections:")
            for i in range(len(sectionsList)):
                print(f"\t{i+1}. {sectionsList[i]}")

        elif userInput == "lsa":
            list = []
            for section in sectionsList:
                list.append(getListOfFiles(section))

            print(f"Files in all sections:")
            for i in range(len(list)):
                obj = list[i]
                print(f"Files in {sectionsList[i]}:")
                for j in range(len(obj)):
                    sobj = obj[j]
                    print(f"\t{j+1}. {sobj['title']} ({sobj['name']})")
                print("")

        elif userInput.startswith("get "):
            section = getSectionFromInput(userInput.split(" ")[1])
            file = getFileFromInput(userInput.split(" ")[2], getListOfFiles(section))
            print(f"Getting file {file} in {section}")
            content = getFileContent(section, file)
            print(f"Content of {file}:\n{content}")

        elif userInput.startswith("rm "):
            section = getSectionFromInput(userInput.split(" ")[1])
            file = getFileFromInput(userInput.split(" ")[2], getListOfFiles(section))
            print(f"Deleting file {file} in {section}")
            content = deleteFile(section, file)
            if content['status'] == 0:
                print("Success")
            else:
                print(f"Error")

        elif userInput == "set":
            settingsState()
            print("Change settings (Leave empty to keep original):")
            newAuthKey = input("\tAuth Key: ")
            newApiURL = input("\tAPI URL: ")
            if newAuthKey != "":
                authKey = newAuthKey
            if newApiURL != "":
                apiURL = newApiURL
            settingsState()

        elif userInput == "help":
            help()

        elif userInput.startswith("copy "):
            section = getSectionFromInput(userInput.split(" ")[1])
            file = getFileFromInput(userInput.split(" ")[2], getListOfFiles(section))
            print(f"Getting file {file} in {section}")
            data = getFileContent(section, file)
            with open(f"{file}", "w") as f:
                f.write(data['data']['content'])

        elif userInput == "exit":
            print("Exiting console...")
            break
    except:
        print("Task failed.")
        print("")