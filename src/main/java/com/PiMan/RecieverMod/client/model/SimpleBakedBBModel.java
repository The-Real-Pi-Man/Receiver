package com.PiMan.RecieverMod.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.UUID;

import javax.annotation.Nullable;
import javax.vecmath.Vector3f;

import com.PiMan.RecieverMod.util.ModelBlock;
import com.PiMan.RecieverMod.util.handlers.ModelElement;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SimpleBakedBBModel implements IBakedModel {

    protected final List<BakedQuad> generalQuads;
    protected final Map<EnumFacing, List<BakedQuad>> faceQuads;
    protected final boolean ambientOcclusion;
    protected final boolean gui3d;
    protected final TextureAtlasSprite texture;
    protected final ItemCameraTransforms cameraTransforms;
    protected final ItemOverrideList itemOverrideList;

    public SimpleBakedBBModel(List<BakedQuad> generalQuadsIn, Map<EnumFacing, List<BakedQuad>> faceQuadsIn, boolean ambientOcclusionIn, boolean gui3dIn, TextureAtlasSprite textureIn, ItemCameraTransforms cameraTransformsIn, ItemOverrideList itemOverrideListIn) {
        this.generalQuads = generalQuadsIn;
        this.faceQuads = faceQuadsIn;
        this.ambientOcclusion = ambientOcclusionIn;
        this.gui3d = gui3dIn;
        this.texture = textureIn;
        this.cameraTransforms = cameraTransformsIn;
        this.itemOverrideList = itemOverrideListIn;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        return side == null ? this.generalQuads : new ArrayList<>();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return this.ambientOcclusion;
    }

    @Override
    public boolean isGui3d() {
        return this.gui3d;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return this.texture;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return this.cameraTransforms;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return this.itemOverrideList;
    }
	
	@SideOnly(Side.CLIENT)
	public static class Builder {
		
		private final ModelBlock model;
		private final ItemOverrideList overrides;
		private final Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter;
		
		public Builder(ModelBlock model, ItemOverrideList overrides, final Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
			this.model = model;
			this.overrides = overrides;
			this.bakedTextureGetter = bakedTextureGetter;
		}
		
		public IBakedModel build() {
			
			List<BakedQuad> quads = new ArrayList<>();
			
			for(Entry<UUID, ModelElement> entry : model.getElements().entrySet()) {
				
				ModelElement element = entry.getValue();
				
				Vector3f[] points = new Vector3f[8];
				
				points[0] = new Vector3f(element.start.x, element.end.y, element.end.z);
				points[1] = new Vector3f(element.end.x, element.end.y, element.end.z);
				points[2] = new Vector3f(element.end.x, element.end.y, element.start.z);
				points[3] = new Vector3f(element.start.x, element.end.y, element.start.z);
				
				points[4] = new Vector3f(element.start.x, element.start.y, element.start.z);
				points[5] = new Vector3f(element.end.x, element.start.y, element.start.z);
				points[6] = new Vector3f(element.end.x, element.start.y, element.end.z);
				points[7] = new Vector3f(element.start.x, element.start.y, element.end.z);
				
				quads.add(new BakedQuadBuilder().setPosition(points[0], 0).setPosition(points[1], 1).setPosition(points[2], 2).setPosition(points[3], 3).applyRotation(element.rotation, element.origin).setColor(0xffffffff).setTexture(bakedTextureGetter.apply(new ResourceLocation(model.resolveTextureName(element.faces.get(EnumFacing.UP).texture)))).setUV(element.faces.get(EnumFacing.UP).uv).build());
				quads.add(new BakedQuadBuilder().setPosition(points[4], 0).setPosition(points[5], 1).setPosition(points[6], 2).setPosition(points[7], 3).applyRotation(element.rotation, element.origin).setColor(0xffffffff).setTexture(bakedTextureGetter.apply(new ResourceLocation(model.resolveTextureName(element.faces.get(EnumFacing.DOWN).texture)))).setUV(element.faces.get(EnumFacing.DOWN).uv).build());
				
				quads.add(new BakedQuadBuilder().setPosition(points[5], 0).setPosition(points[4], 1).setPosition(points[3], 2).setPosition(points[2], 3).applyRotation(element.rotation, element.origin).setColor(0xffffffff).setTexture(bakedTextureGetter.apply(new ResourceLocation(model.resolveTextureName(element.faces.get(EnumFacing.NORTH).texture)))).setUV(element.faces.get(EnumFacing.NORTH).uv).build());
				quads.add(new BakedQuadBuilder().setPosition(points[6], 0).setPosition(points[5], 1).setPosition(points[2], 2).setPosition(points[1], 3).applyRotation(element.rotation, element.origin).setColor(0xffffffff).setTexture(bakedTextureGetter.apply(new ResourceLocation(model.resolveTextureName(element.faces.get(EnumFacing.EAST).texture)))).setUV(element.faces.get(EnumFacing.EAST).uv).build());
				quads.add(new BakedQuadBuilder().setPosition(points[7], 0).setPosition(points[6], 1).setPosition(points[1], 2).setPosition(points[0], 3).applyRotation(element.rotation, element.origin).setColor(0xffffffff).setTexture(bakedTextureGetter.apply(new ResourceLocation(model.resolveTextureName(element.faces.get(EnumFacing.SOUTH).texture)))).setUV(element.faces.get(EnumFacing.SOUTH).uv).build());
				quads.add(new BakedQuadBuilder().setPosition(points[4], 0).setPosition(points[7], 1).setPosition(points[0], 2).setPosition(points[3], 3).applyRotation(element.rotation, element.origin).setColor(0xffffffff).setTexture(bakedTextureGetter.apply(new ResourceLocation(model.resolveTextureName(element.faces.get(EnumFacing.WEST).texture)))).setUV(element.faces.get(EnumFacing.WEST).uv).build());
				
			}
			
			return new SimpleBakedBBModel(quads, null, model.ambientOcclusion, true, bakedTextureGetter.apply(new ResourceLocation(model.textures.get("0"))), model.getAllTransforms(), overrides);
		}
		
		
		
	}

}
