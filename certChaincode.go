package main

import (
	"fmt"
	"encoding/json"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
	"strings"
)

type CertStoreCC struct{}

// 用户
type User struct {
	UserName string `json:"userName"`
	UserId   string `json:"userId"`
	Certs []string `json:"certs"`
}
//证书
type Cert struct {
	CertId string `json:"certId"`
	UserId string `json:"userId"`
	CertName string `json:"name"`
	School string `json:"school"`
	Attrpolic string `json:"attrpolic"`
	CertMessage string `json:"certMessage"`
	HashStr string `json:"hashStr"`
}
//生成用户id
func constructUserKey(userId string) string {
	return fmt.Sprintf("user_%s", userId)
}
//生成证书id
func constructCertKey(assetId string) string {
	return fmt.Sprintf("cert_%s", assetId)
}

func (t *CertStoreCC)  userRegister(stub shim.ChaincodeStubInterface , args []string) pb.Response{

	if len(args) != 2 {
		return shim.Error("not enough args,need name,id")
	}

	// 套路2：验证参数的正确性
	name := args[0]
	id := args[1]
	if name == "" || id == "" {
		return shim.Error("invalid args")
	}

	// 套路3：验证数据是否存在 应该存在 or 不应该存在
	if userBytes, err := stub.GetState(constructUserKey(id)); err == nil && len(userBytes) != 0 {
		return shim.Error("user already exist")
	}

	// 套路4：写入状态
	user := &User{
		UserName:   name,
		UserId:     id,
		Certs: make([]string, 0),
	}

	// 序列化对象
	userBytes, err := json.Marshal(user)
	if err != nil {
		return shim.Error(fmt.Sprintf("marshal user error %s", err))
	}

	if err := stub.PutState(constructUserKey(id), userBytes); err != nil {
		return shim.Error(fmt.Sprintf("put user error %s", err))
	}

	return shim.Success(nil)
}

func (t *CertStoreCC)  userDestroy(stub shim.ChaincodeStubInterface , args []string) pb.Response{

	// 套路1：检查参数的个数
	if len(args) != 1 {
		return shim.Error("not enough args , only need user_id")
	}

	// 套路2：验证参数的正确性
	id := args[0]
	if id == "" {
		return shim.Error("invalid args")
	}

	// 套路3：验证数据是否存在 应该存在 or 不应该存在
	userBytes, err := stub.GetState(constructUserKey(id))
	if err != nil || len(userBytes) == 0 {
		return shim.Error("user not found")
	}

	// 套路4：写入状态
	if err := stub.DelState(constructUserKey(id)); err != nil {
		return shim.Error(fmt.Sprintf("delete user error: %s", err))
	}

	return shim.Success(nil)
}

func (t *CertStoreCC)  certUpload(stub shim.ChaincodeStubInterface , args []string) pb.Response{

	if len(args) != 7 {
		return shim.Error("not enough args,need cert_id,cert_name,school,attrpolic,certMessage,hashStr")
	}

	// 套路2：验证参数的正确性
	certId := args[0]
	userId := args[1]
	certName := args[2]
	school := args[3]
	attrpolic :=args[4]
	certMessage :=args[5]
	hashStr :=args[6]
	if certId == ""||userId == ""||certName==""||school==""||certMessage==""||hashStr=="" {
		return shim.Error("invalid args")
	}

	// 套路3：验证数据是否存在 应该存在 or 不应该存在
	if certBytes, err := stub.GetState(constructCertKey(certId)); err == nil && len(certBytes) != 0 {
		return shim.Error("cert already exist")
	}
	userBytes, err := stub.GetState(constructUserKey(userId))
	if err != nil || len(userBytes) == 0 {
		return shim.Error("user not found")
	}

	// 套路4：写入状态
	certObject := &Cert{
		CertId:   certId,
		UserId:   userId,
		CertName: certName,
		School:     school,
		Attrpolic:  attrpolic,
		CertMessage: certMessage,
		HashStr: hashStr,
	}

	user := new(User)
	if err := json.Unmarshal(userBytes, user); err != nil {
		return shim.Error(fmt.Sprintf("unmarshal user error: %s", err))
	}
	user.Certs = append(user.Certs,"certId:"+certId+"|certName:"+certName+"|school:"+school+"|attrpolic:"+attrpolic)
	// 序列化对象
	certBytes, err := json.Marshal(certObject)
	if err != nil {
		return shim.Error(fmt.Sprintf("marshal cert error %s", err))
	}
	userBytes, err = json.Marshal(user)
	if err != nil {
		return shim.Error(fmt.Sprintf("marshal user error: %s", err))
	}

	//添加状态
	if err := stub.PutState(constructCertKey(certId), certBytes); err != nil {
		return shim.Error(fmt.Sprintf("put cert error %s", err))
	}
	if err := stub.PutState(constructUserKey(user.UserId), userBytes); err != nil {
		return shim.Error(fmt.Sprintf("update user error: %s", err))
	}

	return shim.Success(nil)
}

func (t *CertStoreCC)  certDestroy(stub shim.ChaincodeStubInterface , args []string) pb.Response{

	// 套路1：检查参数的个数
	if len(args) != 1 {
		return shim.Error("not enough args , only need cert_id")
	}

	// 套路2：验证参数的正确性
	id := args[0]
	if id == "" {
		return shim.Error("invalid args")
	}

	// 套路3：验证数据是否存在 应该存在 or 不应该存在
	userBytes, err := stub.GetState(constructCertKey(id))
	if err != nil || len(userBytes) == 0 {
		return shim.Error("cert not found")
	}

	// 套路4：写入状态
	if err := stub.DelState(constructCertKey(id)); err != nil {
		return shim.Error(fmt.Sprintf("delete cert error: %s", err))
	}

	return shim.Success(nil)
}

func (t *CertStoreCC)  queryUser(stub shim.ChaincodeStubInterface , args []string) pb.Response{

	// 套路1：检查参数的个数
	if len(args) != 1 {
		return shim.Error("not enough args,only need user_id")
	}

	// 套路2：验证参数的正确性
	ownerId := args[0]
	if ownerId == "" {
		return shim.Error("invalid args")
	}

	// 套路3：验证数据是否存在 应该存在 or 不应该存在
	userBytes, err := stub.GetState(constructUserKey(ownerId))
	if err != nil || len(userBytes) == 0 {
		return shim.Error("user not found")
	}

	return shim.Success(userBytes)
}

func (t *CertStoreCC)  queryCert(stub shim.ChaincodeStubInterface , args []string) pb.Response{

	// 套路1：检查参数的个数
	if len(args) != 1 {
		return shim.Error("not enough args,only need cert_id")
	}

	// 套路2：验证参数的正确性
	certId := args[0]
	if certId == "" {
		return shim.Error("invalid args")
	}

	// 套路3：验证数据是否存在 应该存在 or 不应该存在
	certBytes, err := stub.GetState(constructCertKey(certId))
	if err != nil || len(certBytes) == 0 {
		return shim.Error("cert not found")
	}

	return shim.Success(certBytes)
}

func (t *CertStoreCC)  queryCertWithUserId(stub shim.ChaincodeStubInterface , args []string) pb.Response{

	// 套路1：检查参数的个数
	if len(args) != 2 {
		return shim.Error("not enough args,only need cert_id,user_id")
	}

	// 套路2：验证参数的正确性
	certId := args[0]
	userId := args[1]
	if certId == "" || userId == ""{
		return shim.Error("invalid args")
	}

	// 套路3：验证数据是否存在 应该存在 or 不应该存在
	certBytes, err := stub.GetState(constructCertKey(certId))
	if err != nil || len(certBytes) == 0 {
		return shim.Error("cert not found")
	}
	userBytes, err := stub.GetState(constructUserKey(userId))
	if err != nil || len(userBytes) == 0 {
		return shim.Error("user not found")
	}
	user := new(User)
	if err := json.Unmarshal(userBytes, user); err != nil {
		return shim.Error(fmt.Sprintf("unmarshal user error: %s", err))
	}
	certs := user.Certs

	for _,value := range certs {
		certIdStr := strings.Split(value,"|")[0]
		curCertId := strings.Split(certIdStr,":")[1]
		if curCertId == certId {
			return shim.Success(certBytes)
		}
	}
	return shim.Error("user not has this cert")
}

func (t *CertStoreCC) Init(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Println("CertChaincode start Init")
	return shim.Success(nil)
}

func (t *CertStoreCC) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Println("CertChaincode Invoke")
	function, args := stub.GetFunctionAndParameters()
	switch function {
	case "userRegister":
		return t.userRegister(stub, args)
	case "userDestroy":
		return t.userDestroy(stub, args)
	case "certUpload":
		return t.certUpload(stub, args)
	case "certDestroy":
		return t.certDestroy(stub, args)
	case "queryUser":
		return t.queryUser(stub, args)
	case "queryCert":
		return t.queryCert(stub, args)
	case "queryCertWithUserId":
		return t.queryCertWithUserId(stub, args)
	default:
		return shim.Error(fmt.Sprintf("unsupported function: %s", function))
	}
}

func main() {
	err := shim.Start(new(CertStoreCC))
	if err != nil {
		fmt.Printf("Error starting Simple chaincode: %s", err)
	}
}
